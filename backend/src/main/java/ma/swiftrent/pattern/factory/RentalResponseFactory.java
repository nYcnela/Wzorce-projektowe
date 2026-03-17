package ma.swiftrent.pattern.factory;

import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.entity.Rental;
import org.springframework.stereotype.Component;

// Tydzień 3, Wzorzec Factory Method 1 – ConcreteCreator dla pary Rental → RentalResponse
@Component
public class RentalResponseFactory extends ResponseFactory<Rental, RentalResponse> {

    @Override
    public RentalResponse create(Rental entity) {
        return RentalResponse.fromEntity(entity);
    }
}
