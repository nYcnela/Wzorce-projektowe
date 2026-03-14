package ma.swiftrent.service;

import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarOperationsService {

    List<CarResponse> getAllCars(String sortBy);

    CarResponse getCarById(Long id);

    CarResponse createCar(CarRequest request, MultipartFile image);

    CarResponse updateCar(Long id, CarRequest request, MultipartFile image);

    void deleteCar(Long id);

    CarResponse duplicateCar(Long id);
}
