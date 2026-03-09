package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.entity.Car;
import ma.swiftrent.pattern.singleton.UploadStorageSettings;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serwis obsługujący operacje na samochodach.
 */
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;
    private final UploadStorageSettings uploadStorageSettings = UploadStorageSettings.getInstance();

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
     * Zapisuje plik na dysku i zwraca jego URL.
     */
    public String storeFile(MultipartFile file) {
        //sprawdzanie czy cos zostalo przeslane
        if (file == null || file.isEmpty()) {
            return null;
        }

        //Pobiera oryginalna nazwe pliku
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new RuntimeException("Nazwa pliku zawiera niepoprawna sekwencje " + fileName);
            }
            //Generuje unikalna nazwe pliku
            String newFileName = UUID.randomUUID().toString() + "_" + fileName;
            //Zapisuje plik na dysku
            Path targetLocation = uploadStorageSettings.resolveTargetLocation(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uploadStorageSettings.buildPublicUrl(newFileName);
        } catch (IOException ex) {
            throw new RuntimeException("Nie mozna zapisac" + fileName + ". Sprobuj ponownie", ex);
        }
    }

    /**
     * Pobiera wszystkie samochody z bazy danych.
     * 
     * @param sortBy Opcjonalny parametr sortowania: price-asc, price-desc
     * @return Lista samochodów posortowana według wybranego kryterium
     */
    @Transactional(readOnly = true)
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

        return cars;
    }

    /**
     * Pobiera samochód po ID.
     */
    @Transactional(readOnly = true)
    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Samochód o ID " + id + " nie został znaleziony"));
        return mapToResponse(car);
    }

    /**
     * Tworzy nowy samochód (ADMIN).
     */
    @Transactional
    public CarResponse createCar(CarRequest request, MultipartFile image) {
        String imageUrl = storeFile(image);
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
    public CarResponse updateCar(Long id, CarRequest request, MultipartFile image) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Samochód o ID " + id + " nie został znaleziony"));

        String imageUrl = storeFile(image);
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
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Samochód o ID " + id + " nie został znaleziony");
        }
        carRepository.deleteById(id);
    }
}
