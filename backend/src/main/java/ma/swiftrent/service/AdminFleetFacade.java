package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.AdminFleetOverviewResponse;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Tydzień 4, Wzorzec Facade 1
// Administrator zarządza flotą samochodów przez jeden, uproszczony punkt wejścia.
// Fasada ukrywa szczegóły operacji CRUD i duplikacji realizowanych przez warstwę serwisów.
@Service
@RequiredArgsConstructor
public class AdminFleetFacade {

    private final CarService carService;

    public AdminFleetOverviewResponse getFleetOverview(String sortBy) {
        List<CarResponse> cars = carService.getAllCars(sortBy);
        long availableCars = cars.stream()
                .filter(CarResponse::isAvailable)
                .count();

        return AdminFleetOverviewResponse.builder()
                .cars(cars)
                .totalCars(cars.size())
                .availableCars(availableCars)
                .unavailableCars(cars.size() - availableCars)
                .build();
    }

    public CarResponse createCar(CarRequest request, MultipartFile image) {
        return carService.createCar(request, image);
    }

    public CarResponse updateCar(Long id, CarRequest request, MultipartFile image) {
        return carService.updateCar(id, request, image);
    }

    public void deleteCar(Long id) {
        carService.deleteCar(id);
    }

    public CarResponse duplicateCar(Long id) {
        return carService.duplicateCar(id);
    }
}
// Koniec, Tydzień 4, Wzorzec Facade 1
