package ma.swiftrent.pattern.iterator.catalog;

import java.util.List;

// Wzorzec Iterator – Użycie 1: przeglądanie katalogu dostępnych aut.
// Konkretny iterator przechodzący po liście dostępnych pojazdów.
public final class AvailableCarCatalogIteratorImpl implements AvailableCarCatalogIterator {

    private final List<String> cars;
    private int index = 0;

    public AvailableCarCatalogIteratorImpl(List<String> cars) {
        this.cars = cars;
    }

    @Override
    public boolean hasNext() {
        return index < cars.size();
    }

    @Override
    public String next() {
        return cars.get(index++);
    }
}
