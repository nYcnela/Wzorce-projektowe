package ma.swiftrent.pattern.factory;

import ma.swiftrent.dto.CarResponse;

import java.util.Comparator;

// Tydzień 3, Wzorzec Factory Method 2 – ConcreteCreator: sortowanie malejące po cenie za dzień
public class PriceDescSortFactory extends CarSortFactory {

    @Override
    public Comparator<CarResponse> createComparator() {
        return Comparator.comparing(CarResponse::getPricePerDay).reversed();
    }
}
