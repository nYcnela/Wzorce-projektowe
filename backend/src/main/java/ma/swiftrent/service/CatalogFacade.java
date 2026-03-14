package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarAvailabilityResponse;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.dto.CatalogViewResponse;
import ma.swiftrent.dto.RentalResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

// Tydzień 4, Wzorzec Facade 2
// Klient przegląda katalog przez jeden punkt wejścia, bez znajomości wielu serwisów.
// Fasada składa odpowiedzi katalogowe z danych o samochodach, ulubionych i terminach zajętości.
@Service
@RequiredArgsConstructor
public class CatalogFacade {

    private final CarService carService;
    private final RentalService rentalService;
    private final UserService userService;

    public List<CarResponse> getPublicCatalog(String sortBy) {
        return carService.getAllCars(sortBy);
    }

    public CarResponse getCarDetails(Long carId) {
        return carService.getCarById(carId);
    }

    public CatalogViewResponse getUserCatalog(String userEmail, String sortBy) {
        List<CarResponse> cars = carService.getAllCars(sortBy);
        List<CarResponse> favoriteCars = userService.getUserFavorites(userEmail);

        return CatalogViewResponse.builder()
                .cars(cars)
                .favoriteCars(favoriteCars)
                .totalCars(cars.size())
                .favoriteCount(favoriteCars.size())
                .build();
    }

    public CatalogViewResponse getPublicCatalogView(String sortBy) {
        List<CarResponse> cars = carService.getAllCars(sortBy);

        return CatalogViewResponse.builder()
                .cars(cars)
                .favoriteCars(Collections.emptyList())
                .totalCars(cars.size())
                .favoriteCount(0)
                .build();
    }

    public CarAvailabilityResponse getCarAvailability(Long carId) {
        CarResponse car = carService.getCarById(carId);
        List<RentalResponse> occupiedDates = rentalService.getOccupiedDates(carId);

        return CarAvailabilityResponse.builder()
                .car(car)
                .occupiedDates(occupiedDates)
                .occupiedPeriods(occupiedDates.size())
                .build();
    }
}
// Koniec, Tydzień 4, Wzorzec Facade 2
