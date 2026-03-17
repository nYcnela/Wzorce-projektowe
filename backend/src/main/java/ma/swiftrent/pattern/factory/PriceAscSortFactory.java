package ma.swiftrent.pattern.factory;

import ma.swiftrent.dto.CarResponse;

import java.util.Comparator;

// Tydzień 3, Wzorzec Factory Method 2 – ConcreteCreator: sortowanie rosnące po cenie za dzień
public class PriceAscSortFactory extends CarSortFactory {

    @Override
    public Comparator<CarResponse> createComparator() {
        return Comparator.comparing(CarResponse::getPricePerDay);
    }
}
