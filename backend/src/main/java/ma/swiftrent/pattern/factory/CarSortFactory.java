package ma.swiftrent.pattern.factory;

import ma.swiftrent.dto.CarResponse;

import java.util.Comparator;

/*
    Tydzień 3, Wzorzec Factory Method 2
    Abstrakcyjna fabryka komparatorów do sortowania listy samochodów.
    Metoda fabryczna createComparator() jest abstrakcyjna – podklasa decyduje
    według jakiego kryterium posortować listę.
    Statyczna metoda forStrategy() wybiera konkretną fabrykę
    na podstawie przekazanego kodu strategii sortowania.
*/
public abstract class CarSortFactory {

    // Metoda fabryczna – nadpisywana przez podklasy (ConcreteCreator)
    public abstract Comparator<CarResponse> createComparator();

    // Wybiera odpowiednią fabrykę na podstawie nazwy strategii
    public static CarSortFactory forStrategy(String sortBy) {
        if (sortBy == null) {
            return new DefaultSortFactory();
        }
        return switch (sortBy) {
            case "price-asc"  -> new PriceAscSortFactory();
            case "price-desc" -> new PriceDescSortFactory();
            default           -> new DefaultSortFactory();
        };
    }
}
