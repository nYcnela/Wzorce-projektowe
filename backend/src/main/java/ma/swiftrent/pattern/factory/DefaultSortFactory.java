package ma.swiftrent.pattern.factory;

import ma.swiftrent.dto.CarResponse;

import java.util.Comparator;

// Tydzień 3, Wzorzec Factory Method 2 – ConcreteCreator: brak sortowania (kolejność po ID)
public class DefaultSortFactory extends CarSortFactory {

    @Override
    public Comparator<CarResponse> createComparator() {
        return Comparator.comparing(CarResponse::getId);
    }
}
