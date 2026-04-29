package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.composite.rentalPackage.RentalPackage;
import ma.swiftrent.composite.rentalPackage.RentalServiceItem;
import ma.swiftrent.dto.RentalPackageSummaryResponse;
import ma.swiftrent.dto.RentalRequest;
import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.entity.Car;
import ma.swiftrent.entity.Rental;
import ma.swiftrent.entity.User;
import ma.swiftrent.pattern.bridge.notification.*;
import ma.swiftrent.pattern.bridge.report.CsvFormatter;
import ma.swiftrent.pattern.bridge.report.RentalReport;
import ma.swiftrent.pattern.bridge.report.Report;
import ma.swiftrent.pattern.bridge.storage.*;
import ma.swiftrent.pattern.factory.RentalResponseFactory;
import ma.swiftrent.pattern.observer.rentalcreated.RentalCreatedEvent;
import ma.swiftrent.pattern.observer.rentalcreated.RentalCreatedSubject;
import ma.swiftrent.pattern.observer.rentalstatus.RentalStatusChangedEvent;
import ma.swiftrent.pattern.observer.rentalstatus.RentalStatusChangedSubject;
import ma.swiftrent.pattern.strategy.access.RentalAccessStrategyContext;
import ma.swiftrent.pattern.strategy.pricing.RentalPricingStrategyContext;
import ma.swiftrent.pattern.state.car.CarAvailabilityStateContext;
import ma.swiftrent.pattern.state.rental.RentalStateContext;
import ma.swiftrent.pattern.singleton.ApplicationClock;
import ma.swiftrent.pattern.singleton.SecurityContextAccessor;
import ma.swiftrent.pattern.visitor.rentalpackage.RentalDescriptionVisitor;
import ma.swiftrent.pattern.visitor.rentalpackage.RentalItemCountVisitor;
import ma.swiftrent.pattern.visitor.rentalpackage.RentalPriceVisitor;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.repository.UserRepository;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.EmailNotificationDecorator;
import ma.swiftrent.service.notification.NotificationService;
import ma.swiftrent.pattern.bridge.report.CsvFormatter;
import ma.swiftrent.pattern.bridge.report.RentalReport;
import ma.swiftrent.pattern.bridge.report.Report;
import ma.swiftrent.service.notification.SmsNotificationDecorator;
import ma.swiftrent.service.price.BasicRentalPrice;
import ma.swiftrent.service.price.GpsDecorator;
import ma.swiftrent.service.price.InsuranceDecorator;
import ma.swiftrent.service.price.RentalPrice;
import ma.swiftrent.service.storage.JsonStorageAdapter;
import ma.swiftrent.service.storage.JsonStorageSystem;
import ma.swiftrent.service.storage.LocalFileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serwis obsługujący operacje na wypożyczeniach.
 */
@Service
@RequiredArgsConstructor
public class RentalService {
    AppLogger appLogger = new ConsoleLogger();
    NotificationService notification = new BasicNotificationService();
    private NotificationServiceFactory notificationServiceFactory = new NotificationServiceFactory();
    NotificationComponent notification2 = notificationServiceFactory.createNotificationSystem();

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalCreatedSubject rentalCreatedSubject;
    private final RentalStatusChangedSubject rentalStatusChangedSubject;
    private final ApplicationClock applicationClock = ApplicationClock.getInstance();
    // Tydzień 6, Wzorzec Strategy 1 – użycie RentalPricingStrategyContext (Context)
    private final RentalPricingStrategyContext rentalPricingStrategyContext = new RentalPricingStrategyContext();
    // Tydzień 6, Wzorzec Strategy 2 – użycie RentalAccessStrategyContext (Context)
    private final RentalAccessStrategyContext rentalAccessStrategyContext = new RentalAccessStrategyContext();
    private final SecurityContextAccessor securityContextAccessor = SecurityContextAccessor.getInstance();
    // Tydzień 6, Wzorzec State 1 – użycie RentalStateContext (Context)
    private final RentalStateContext rentalStateContext = new RentalStateContext();
    // Tydzień 6, Wzorzec State 2 – użycie CarAvailabilityStateContext (Context)
    private final CarAvailabilityStateContext carAvailabilityStateContext = new CarAvailabilityStateContext();

    // Tydzień 3, Wzorzec Factory Method 2 – użycie RentalResponseFactory (ConcreteCreator)
    private final RentalResponseFactory rentalResponseFactory = new RentalResponseFactory();

    @Transactional
    public RentalResponse createRental(RentalRequest request) {
        // Pobiera aktualnie zalogowanego użytkownika
        String userEmail = securityContextAccessor.getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie został znaleziony"));

        // Walidacja dat
        validateDates(request.getStartDate(), request.getEndDate());

        // Znajduje samochód
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Samochód o ID " + request.getCarId() + " nie został znaleziony"));

        // Sprawdza kolizje rezerwacji
        if (rentalRepository.existsOverlappingRental(car.getId(), request.getStartDate(), request.getEndDate())) {
            throw new RuntimeException("Samochód jest już zarezerwowany w podanym terminie");
        }

        RentalPrice price = new BasicRentalPrice(car.getPricePerDay(), request.getStartDate(), request.getEndDate());
        if (request.getInsuranceSelected()) {
            price = new InsuranceDecorator(price);
        }
        if (request.getGpsSelected()) {
            price = new GpsDecorator(price);
        }
        BigDecimal baseCost = price.calculateTotalCost();
        // Tydzień 6, Wzorzec Strategy 1 – wybór algorytmu naliczania końcowej ceny
        BigDecimal totalCost = rentalPricingStrategyContext
                .resolve(user, request.getStartDate(), request.getEndDate())
                .calculate(baseCost);

        // Tworzy wypożyczenie
        Rental rental = Rental.builder()
                .user(user)
                .car(car)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalCost(totalCost)
                .active()
                .build();

        appLogger = new TimestampLoggerDecorator(appLogger);
        appLogger.logInfo("Dokonano rezerwacji.");
        notification = new EmailNotificationDecorator(notification);
        notification = new SmsNotificationDecorator(notification);
        notification.send("Zarejestrowano na twoim konice nowe wypożyczenie.");
        notification2.send("Samochód został wypożyczony przez: " + user.getEmail());

        // Zmienia status samochodu na zajęty TYLKO jeśli wypożyczenie zaczyna się dzisiaj
        if (request.getStartDate().isEqual(applicationClock.today())) {
            // Tydzień 6, Wzorzec State 2 – stan auta decyduje o zmianie dostępności
            carAvailabilityStateContext.resolve(car).markUnavailable(car);
            carRepository.save(car);
        }

        Rental savedRental = rentalRepository.save(rental);
        rentalCreatedSubject.notifyObservers(new RentalCreatedEvent(
                savedRental.getId(),
                car.getId(),
                user.getEmail(),
                savedRental.getTotalCost()
        ));
        return rentalResponseFactory.create(savedRental);
    }

    /**
     * Zwraca wypożyczony samochód (zakończenie wypożyczenia).
     *
     * @param id ID wypożyczenia
     */
    @Transactional
    public void returnRental(Long id) {
        String userEmail = securityContextAccessor.getCurrentUserEmail();
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wypożyczenie nie znalezione"));
        Rental.RentalStatus previousStatus = rental.getStatus();

        // Sprawdza czy użytkownik jest właścicielem (chyba że to admin)
        boolean isAdmin = securityContextAccessor.currentUserHasRole("ADMIN");
        // Tydzień 6, Wzorzec Strategy 2 – strategia wybiera sposób weryfikacji dostępu
        rentalAccessStrategyContext.resolve(isAdmin, rental, userEmail).validate(rental, userEmail);

        // Tydzień 6, Wzorzec State 1 – delegacja operacji do obiektu stanu
        rentalStateContext.resolve(rental).returnRental(rental, applicationClock);
        carRepository.save(rental.getCar());
        rentalRepository.save(rental);
        rentalStatusChangedSubject.notifyObservers(new RentalStatusChangedEvent(
                rental.getId(),
                rental.getCar().getId(),
                userEmail,
                previousStatus,
                rental.getStatus()
        ));
    }

    /**
     * Anuluje rezerwację (tylko jeśli jeszcze się nie rozpoczęła).
     *
     * @param id ID wypożyczenia
     */
    @Transactional
    public void cancelRental(Long id) {
        String userEmail = securityContextAccessor.getCurrentUserEmail();
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wypożyczenie nie znalezione"));
        Rental.RentalStatus previousStatus = rental.getStatus();

        // Sprawdza czy użytkownik jest właścicielem (chyba że to admin)
        boolean isAdmin = securityContextAccessor.currentUserHasRole("ADMIN");
        // Tydzień 6, Wzorzec Strategy 2 – strategia wybiera sposób weryfikacji dostępu
        rentalAccessStrategyContext.resolve(isAdmin, rental, userEmail).validate(rental, userEmail);

        // Tydzień 6, Wzorzec State 1 – delegacja operacji do obiektu stanu
        rentalStateContext.resolve(rental).cancelRental(rental, applicationClock);
        rentalRepository.save(rental);
        rentalStatusChangedSubject.notifyObservers(new RentalStatusChangedEvent(
                rental.getId(),
                rental.getCar().getId(),
                userEmail,
                previousStatus,
                rental.getStatus()
        ));
    }

    /**
     * Pobiera wszystkie wypożyczenia zalogowanego użytkownika.
     *
     * @return Lista wypożyczeń użytkownika
     */
    @Transactional(readOnly = true)
    public List<RentalResponse> getUserRentals() {
        String userEmail = securityContextAccessor.getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie został znaleziony"));

        return rentalResponseFactory.createAll(rentalRepository.findByUserId(user.getId()));
    }

    /**
     * Pobiera wszystkie wypożyczenia (ADMIN).
     *
     * @return Lista wszystkich wypożyczeń
     */
    @Transactional(readOnly = true)
    public List<RentalResponse> getAllRentals() {
        return rentalResponseFactory.createAll(rentalRepository.findAll());
    }

    /**
     * Pobiera listy zajętych dat dla konkretnego samochodu.
     */
    @Transactional(readOnly = true)
    public List<RentalResponse> getOccupiedDates(Long carId) {
        return rentalRepository.findAll().stream()
                .filter(r -> r.getCar().getId().equals(carId))
                .filter(r -> rentalStateContext.resolve(r).blocksAvailability())
                .map(rentalResponseFactory::create)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RentalPackageSummaryResponse getPremiumPackageSummary(double carPricePerDay, int days) {
        RentalPackage premiumPackage = createPremiumPackage(carPricePerDay, days);

        // Tydzień 6, Wzorzec Visitor 1 – użycie RentalPriceVisitor (ConcreteVisitor)
        RentalPriceVisitor priceVisitor = new RentalPriceVisitor();
        premiumPackage.accept(priceVisitor);

        // Tydzień 6, Wzorzec Visitor 2 – użycie RentalDescriptionVisitor (ConcreteVisitor)
        RentalDescriptionVisitor descriptionVisitor = new RentalDescriptionVisitor();
        premiumPackage.accept(descriptionVisitor);

        // Tydzień 6, Wzorzec Visitor 3 – użycie RentalItemCountVisitor (ConcreteVisitor)
        RentalItemCountVisitor countVisitor = new RentalItemCountVisitor();
        premiumPackage.accept(countVisitor);

        return RentalPackageSummaryResponse.builder()
                .packageName(premiumPackage.getName())
                .totalPrice(priceVisitor.getTotalPrice())
                .description(descriptionVisitor.getDescription())
                .packageCount(countVisitor.getPackageCount())
                .serviceItemCount(countVisitor.getServiceItemCount())
                .build();
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = applicationClock.today();
        LocalDate maxDate = today.plusMonths(1);

        // Data rozpoczęcia nie może być w przeszłości (ale dzisiaj jest OK)
        if (startDate.isBefore(today)) {
            throw new RuntimeException("Data rozpoczęcia nie może być w przeszłości");
        }

        // Nie można rezerwować dalej niż na miesiąc w przód
        if (startDate.isAfter(maxDate)) {
            throw new RuntimeException("Można rezerwować tylko do miesiąca w przód (max: " + maxDate + ")");
        }

        // Data zakończenia musi być po dacie rozpoczęcia
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new RuntimeException("Data zakończenia musi być po dacie rozpoczęcia");
        }

        if (endDate.isAfter(maxDate)) {
            throw new RuntimeException("Wypożyczenie może trwać maksymalnie do " + maxDate);
        }
    }

    /**
     * Oblicza całkowity koszt wypożyczenia.
     *
     * @param pricePerDay Cena za dzień
     * @param startDate Data rozpoczęcia
     * @param endDate Data zakończenia
     * @return Całkowity koszt wypożyczenia
     */
    private BigDecimal calculateTotalCost(BigDecimal pricePerDay, LocalDate startDate, LocalDate endDate) {
        // Liczenie dni włącznie: z 8/01 na 9/01 = 2 dni (8/01 + 9/01)
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return pricePerDay.multiply(BigDecimal.valueOf(days));
    }

    /**
     * Duplikuje wypożyczenie wykorzystując do tego wzorzec prototypu
     */
    @Transactional
    public RentalResponse duplicateRental(Long id) {

        Rental original = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wypożyczenie o ID " + id + " nie istnieje"));

        Rental copy = original.clone();
        copy.setId(null);
        Rental saved = rentalRepository.save(copy);
        return RentalResponse.fromEntity(saved);
    }
/*
Tydzień 9, Niedługie metody 1
Te metoda nie przekracza 20 linii,
jest blisko tej liczby, ale to tylko ze względu na spacje dla przejrzystości,
zajmuje 19 linii

Tydzień 9, Max 3 argumenty 2
Ta funkcja posiada tylko 2 argumenty
 */
    public RentalPackage createPremiumPackage(double carPricePerDay, int days) {

        RentalServiceItem carRental =
                new RentalServiceItem("Wynajem samochodu", carPricePerDay * days);

        RentalServiceItem insurance =
                new RentalServiceItem("Ubezpieczenie", 50);

        RentalServiceItem gps =
                new RentalServiceItem("GPS", 20);

        RentalPackage premiumPackage = new RentalPackage("Pakiet Premium");

        premiumPackage.add(carRental);
        premiumPackage.add(insurance);
        premiumPackage.add(gps);

        return premiumPackage;
    }
//Koniec, Tydzień 9, Niedługie metody 1
//Koniec, Tydzień 9, Max 3 argumenty 2
    public void informUser(User user){
        Notification rentalNotification = new RentalNotification(new EmailSender());
        rentalNotification.send("Samochód został wypożyczony przez: " + user.getEmail());

        Notification userNotification = new UserNotification(new SmsSender());
        userNotification.send("Dodano wypożyczenie na twoje konto.");
    }

    public String storeFile(MultipartFile file) {

        StorageImplementor implementor = new JsonStorageImplementor(
                new JsonStorageAdapter(
                        new JsonStorageSystem()
                )
        );

        FileStorage storage = new DocumentStorage(implementor);

        return storage.store(file);
    }
}
