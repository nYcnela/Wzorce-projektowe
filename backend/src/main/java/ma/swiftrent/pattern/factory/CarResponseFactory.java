package ma.swiftrent.pattern.factory;

import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.entity.Car;
import org.springframework.stereotype.Component;

// Tydzień 3, Wzorzec Factory Method 1 – ConcreteCreator dla pary Car → CarResponse
@Component
public class CarResponseFactory extends ResponseFactory<Car, CarResponse> {

    @Override
    public CarResponse create(Car entity) {
        return CarResponse.fromEntity(entity);
    }
}
