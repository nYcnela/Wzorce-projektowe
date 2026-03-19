package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.entity.Car;
import ma.swiftrent.pattern.factory.CarResponseFactory;
import ma.swiftrent.pattern.factory.CarSortFactory;
import ma.swiftrent.pattern.state.car.CarAvailabilityState;
import ma.swiftrent.pattern.state.car.CarAvailabilityStateContext;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.LoggerInheritanceAdapter;
import ma.swiftrent.service.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serwis obsługujący operacje na samochodach.
 */
@Service("carOperationsServiceTarget")
@RequiredArgsConstructor
public class CarService implements CarOperationsService {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;
    private final FileStorageService fileStorageService;
    private final AppLogger logger = new LoggerInheritanceAdapter();
    // Tydzień 6, Wzorzec State 2 – użycie CarAvailabilityStateContext (Context)
    private final CarAvailabilityStateContext carAvailabilityStateContext = new CarAvailabilityStateContext();

    // Tydzień 3, Wzorzec Factory Method 1 – użycie CarResponseFactory (ConcreteCreator)
    private final CarResponseFactory carResponseFactory = new CarResponseFactory();

    private CarResponse mapToResponse(Car car) {
        CarResponse response = carResponseFactory.create(car);
        // Tydzień 6, Wzorzec State 2 – kontekst wybiera stan dostępności auta
        CarAvailabilityState availabilityState = carAvailabilityStateContext.resolve(car);
        if (!availabilityState.isAvailable()) {
            LocalDate availableFrom = availabilityState.resolveAvailableFrom(
                    rentalRepository.findLatestEndDate(car.getId())
            );
            if (availableFrom != null) {
                response.setAvailableFrom(availableFrom);
            }
        }
        return response;
    }

    /**
     * Pobiera wszystkie samochody z bazy danych.
     * 
     * @param sortBy Opcjonalny parametr sortowania: price-asc, price-desc
     * @return Lista samochodów posortowana według wybranego kryterium
     */
    @Transactional(readOnly = true)
    @Override
    public List<CarResponse> getAllCars(String sortBy) {
        //Pobiera wszystkie samochody i mapuje na dto
        List<CarResponse> cars = carRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        // Tydzień 3, Wzorzec Factory Method 2 – CarSortFactory tworzy odpowiedni komparator
        cars.sort(CarSortFactory.forStrategy(sortBy).createComparator());

        return cars;
    }

    /**
     * Pobiera samochód po ID.
     */
    @Transactional(readOnly = true)
    @Override
    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Samochód o ID " + id + " nie został znaleziony"));
        return mapToResponse(car);
    }

    /**
     * Tworzy nowy samochód (ADMIN).
     */
    @Transactional
    @Override
    public CarResponse createCar(CarRequest request, MultipartFile image) {
        String imageUrl = fileStorageService.store(image);
        if (imageUrl == null) {
            imageUrl = request.getImageUrl();
        }

        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .pricePerDay(request.getPricePerDay())
                .productionYear(request.getProductionYear())
                .color(request.getColor())
                .imageUrl(imageUrl)
                .available()
                .build();

        Car savedCar = carRepository.save(car);
        return mapToResponse(savedCar);
    }

    /**
     * Aktualizuje dane samochodu (ADMIN).
     */
    @Transactional
    @Override
    public CarResponse updateCar(Long id, CarRequest request, MultipartFile image) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Samochód o ID " + id + " nie został znaleziony"));

        String imageUrl = fileStorageService.store(image);
        if (imageUrl != null) {
            car.setImageUrl(imageUrl);
        } else if (request.getImageUrl() != null) {
            car.setImageUrl(request.getImageUrl());
        }

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setPricePerDay(request.getPricePerDay());
        car.setProductionYear(request.getProductionYear());
        car.setColor(request.getColor());

        Car updatedCar = carRepository.save(car);
        return mapToResponse(updatedCar);
    }

    /**
     * Usuwa samochód z bazy danych (ADMIN).
     */
    @Transactional
    @Override
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Samochód o ID " + id + " nie został znaleziony");
        }
        logger.logInfo("Usunięto samochód");
        carRepository.deleteById(id);
    }

    /**
     * Duplikuje samochód wykorzystując do tego wzorzec prototypu
     */
    @Transactional
    @Override
    public CarResponse duplicateCar(Long id) {

        Car original = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Samochód o ID " + id + " nie istnieje"));

        Car copy = original.clone();
        copy.setId(null);

        logger.logInfo("Zduplikowano samochód");
        Car saved = carRepository.save(copy);

        return mapToResponse(saved);
    }
}
