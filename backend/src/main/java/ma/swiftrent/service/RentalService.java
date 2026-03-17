package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.composite.rentalPackage.RentalPackage;
import ma.swiftrent.composite.rentalPackage.RentalServiceItem;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.dto.RentalRequest;
import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.entity.Car;
import ma.swiftrent.entity.Rental;
import ma.swiftrent.entity.User;
import ma.swiftrent.pattern.factory.CancelRentalActionFactory;
import ma.swiftrent.pattern.factory.RentalResponseFactory;
import ma.swiftrent.pattern.factory.ReturnRentalActionFactory;
import ma.swiftrent.pattern.singleton.ApplicationClock;
import ma.swiftrent.pattern.singleton.SecurityContextAccessor;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.repository.UserRepository;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.EmailNotificationDecorator;
import ma.swiftrent.service.notification.NotificationService;
import ma.swiftrent.service.notification.SmsNotificationDecorator;
import ma.swiftrent.service.price.BasicRentalPrice;
import ma.swiftrent.service.price.GpsDecorator;
import ma.swiftrent.service.price.InsuranceDecorator;
import ma.swiftrent.service.price.RentalPrice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final ApplicationClock applicationClock = ApplicationClock.getInstance();
    private final SecurityContextAccessor securityContextAccessor = SecurityContextAccessor.getInstance();
    RentalPrice price;
    AppLogger appLogger = new ConsoleLogger();
    NotificationService notification = new BasicNotificationService();
    private NotificationServiceFactory notificationServiceFactory = new NotificationServiceFactory();
    NotificationComponent notification2 = notificationServiceFactory.createNotificationSystem();

    // Tydzień 3, Wzorzec Factory Method 2 – użycie RentalResponseFactory (ConcreteCreator)
    private final RentalResponseFactory rentalResponseFactory = new RentalResponseFactory();

    /**
     * Tworzy nowe wypożyczenie samochodu.
     *
     * @param request Dane wypożyczenia
     * @return Utworzone wypożyczenie
     * @throws RuntimeException gdy walidacja nie powiedzie się
     */
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

        // Oblicza całkowity koszt
        price = new BasicRentalPrice(car.getPricePerDay(), request.getStartDate(), request.getEndDate());
        if (request.getInsuranceSelected()) {
            price = new InsuranceDecorator(price);
        }
        if (request.getGpsSelected()) {
            price = new GpsDecorator(price);
        }
        BigDecimal totalCost = price.calculateTotalCost();
//        BigDecimal totalCost = calculateTotalCost(car.getPricePerDay(), request.getStartDate(), request.getEndDate());

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
            car.setStatus(Car.CarStatus.UNAVAILABLE);
            carRepository.save(car);
        }

        Rental savedRental = rentalRepository.save(rental);
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

        // Sprawdza czy użytkownik jest właścicielem (chyba że to admin)
        boolean isAdmin = securityContextAccessor.currentUserHasRole("ADMIN");

        if (!isAdmin && !rental.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Nie masz uprawnień do zwrotu tego wypożyczenia");
        }

        if (rental.getStatus() != Rental.RentalStatus.ACTIVE) {
            throw new RuntimeException("Wypożyczenie nie jest aktywne");
        }

        // Tydzień 3, Wzorzec Factory Method 3 – fabryka tworzy i wykonuje akcję zwrotu
        new ReturnRentalActionFactory(applicationClock).process(rental);
        carRepository.save(rental.getCar());
        rentalRepository.save(rental);
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

        // Sprawdza czy użytkownik jest właścicielem (chyba że to admin)
        boolean isAdmin = securityContextAccessor.currentUserHasRole("ADMIN");

        if (!isAdmin && !rental.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Nie masz uprawnień do anulowania tego wypożyczenia");
        }

        if (rental.getStatus() != Rental.RentalStatus.ACTIVE) {
            throw new RuntimeException("Tylko aktywne rezerwacje można anulować");
        }

        LocalDate today = applicationClock.today();
        
        // Można anulować tylko rezerwacje, które jeszcze się nie rozpoczęły
        if (!rental.getStartDate().isAfter(today)) {
            throw new RuntimeException("Nie można anulować wypożyczenia, które już się rozpoczęło. Użyj opcji zwrotu.");
        }

        // Tydzień 3, Wzorzec Factory Method 3 – fabryka tworzy i wykonuje akcję anulowania
        new CancelRentalActionFactory().process(rental);
        rentalRepository.save(rental);
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
                .filter(r -> r.getStatus() != Rental.RentalStatus.CANCELLED && r.getStatus() != Rental.RentalStatus.COMPLETED)
                .map(rentalResponseFactory::create)
                .collect(Collectors.toList());
    }

    /**
     * Walidacja dat wypożyczenia.
     *
     * @param startDate Data rozpoczęcia
     * @param endDate Data zakończenia
     * @throws RuntimeException gdy daty są nieprawidłowe
     */
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
}
