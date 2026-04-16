package ma.swiftrent.pattern.iterator.catalog;

// Wzorzec Iterator – Użycie 1: przeglądanie katalogu dostępnych aut.
// Interfejs iteratora pozwala sekwencyjnie przechodzić po katalogu pojazdów.
public interface AvailableCarCatalogIterator {
    boolean hasNext();
    String next();
}
