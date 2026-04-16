package ma.swiftrent.pattern.iterator.history;

// Wzorzec Iterator – Użycie 2: przeglądanie historii wypożyczeń użytkownika.
// Interfejs iteratora umożliwia sekwencyjny odczyt historii wynajmów.
public interface UserRentalHistoryIterator {
    boolean hasNext();
    String next();
}
