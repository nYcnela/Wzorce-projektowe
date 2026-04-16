package ma.swiftrent.pattern.iterator.history;

import java.util.List;

// Wzorzec Iterator – Użycie 2: przeglądanie historii wypożyczeń użytkownika.
// Konkretny iterator przechodzący po wpisach historii od najnowszego.
public final class UserRentalHistoryIteratorImpl implements UserRentalHistoryIterator {

    private final List<String> history;
    private int index;

    public UserRentalHistoryIteratorImpl(List<String> history) {
        this.history = history;
        this.index = history.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return index >= 0;
    }

    @Override
    public String next() {
        return history.get(index--);
    }
}
