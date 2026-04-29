package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Tydzień 4, Wzorzec Proxy 2
// Odczyty samochodów przechodzą przez pośrednika, który przechowuje ostatnio pobrane wyniki w pamięci.
// Proxy ukrywa cache przed fasadami i jednocześnie czyści go po operacjach modyfikujących flotę.
@Service
@Primary
@RequiredArgsConstructor
public class CachedCarServiceProxy implements CarOperationsService {

    private static final String DEFAULT_SORT_KEY = "default";

    @Qualifier("carOperationsServiceTarget")
    private final CarOperationsService carOperationsService;

    private final Map<String, List<CarResponse>> allCarsCache = new ConcurrentHashMap<>();
    private final Map<Long, CarResponse> carByIdCache = new ConcurrentHashMap<>();

    @Override
    public List<CarResponse> getAllCars(String sortBy) {
        String cacheKey = sortBy == null ? DEFAULT_SORT_KEY : sortBy;
        List<CarResponse> cachedCars = allCarsCache.computeIfAbsent(
                cacheKey,
                ignored -> new ArrayList<>(carOperationsService.getAllCars(sortBy))
        );
        return new ArrayList<>(cachedCars);
    }

    @Override
    public CarResponse getCarById(Long id) {
        return carByIdCache.computeIfAbsent(id, carOperationsService::getCarById);
    }

    @Override
    public CarResponse createCar(CarRequest request, MultipartFile image) {
        CarResponse createdCar = carOperationsService.createCar(request, image);
        clearCache();
        return createdCar;
    }

    /*
    Tydzień 9, Max 3 argumenty 3
    Ta funkcja posiada 3 parametry
     */
    @Override
    public CarResponse updateCar(Long id, CarRequest request, MultipartFile image) {
        CarResponse updatedCar = carOperationsService.updateCar(id, request, image);
        clearCache();
        return updatedCar;
    }
    //Koniec, Tydzień 9, Max 3 argumenty 3

    @Override
    public void deleteCar(Long id) {
        carOperationsService.deleteCar(id);
        clearCache();
    }

    @Override
    public CarResponse duplicateCar(Long id) {
        CarResponse duplicatedCar = carOperationsService.duplicateCar(id);
        clearCache();
        return duplicatedCar;
    }

    private void clearCache() {
        allCarsCache.clear();
        carByIdCache.clear();
    }
}
// Koniec, Tydzień 4, Wzorzec Proxy 2
