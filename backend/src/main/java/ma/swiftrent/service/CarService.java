package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.entity.Car;
import ma.swiftrent.pattern.bridge.report.CarReport;
import ma.swiftrent.pattern.bridge.report.JsonFormatter;
import ma.swiftrent.pattern.bridge.report.Report;
import ma.swiftrent.pattern.bridge.storage.FileStorage;
import ma.swiftrent.pattern.bridge.storage.ImageStorage;
import ma.swiftrent.pattern.bridge.storage.LocalStorageImplementor;
import ma.swiftrent.pattern.bridge.storage.StorageImplementor;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.LoggerInheritanceAdapter;
import ma.swiftrent.service.storage.FileStorageService;
import ma.swiftrent.service.storage.JsonStorageAdapter;
import ma.swiftrent.service.storage.JsonStorageSystem;
import ma.swiftrent.service.storage.LocalFileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private CarResponse mapToResponse(Car car) {
        CarResponse response = CarResponse.fromEntity(car);
        if (car.getStatus() != Car.CarStatus.AVAILABLE) {
            java.time.LocalDate latestEnd = rentalRepository.findLatestEndDate(car.getId());
            if (latestEnd != null) {
                response.setAvailableFrom(latestEnd.plusDays(1));
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

        if (sortBy != null) {
            switch (sortBy) {
                case "price-asc":
                    cars.sort((a, b) -> a.getPricePerDay().compareTo(b.getPricePerDay()));
                    break;
                case "price-desc":
                    cars.sort((a, b) -> b.getPricePerDay().compareTo(a.getPricePerDay()));
                    break;
                default:
                    // Brak sortowania
                    break;
            }
        }

        Report report = new CarReport(cars, new JsonFormatter());
        String result = report.generate();
        System.out.println(result);

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

    public String uploadCarFile(MultipartFile file, String type) {

        FileStorageService storage;

        if ("json".equalsIgnoreCase(type)) {
            storage = new JsonStorageAdapter(new JsonStorageSystem());
        } else {
            storage = new LocalFileStorageService();
        }

        return storage.store(file);
    }

    public String storeFile(MultipartFile file) {

        StorageImplementor implementor = new LocalStorageImplementor(new LocalFileStorageService());

        FileStorage storage = new ImageStorage(implementor);

        return storage.store(file);
    }
}
