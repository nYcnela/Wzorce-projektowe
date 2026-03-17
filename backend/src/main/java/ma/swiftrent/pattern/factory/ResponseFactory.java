package ma.swiftrent.pattern.factory;

import java.util.List;
import java.util.stream.Collectors;

/*
    Tydzień 3, Wzorzec Factory Method 1
    Abstrakcyjna fabryka konwertująca encje JPA na obiekty DTO.
    Metoda fabryczna create() jest abstrakcyjna – podklasa decyduje
    jak przeprowadzić konwersję dla konkretnej pary (encja → DTO).
    Metoda createAll() jest metodą szablonową, która wielokrotnie
    wywołuje create() bez wiedzy o konkretnym typie produktu.
*/
public abstract class ResponseFactory<E, R> {

    // Metoda fabryczna – nadpisywana przez podklasy (ConcreteCreator)
    public abstract R create(E entity);

    // Metoda szablonowa – używa metody fabrycznej do przetworzenia całej listy
    public List<R> createAll(List<E> entities) {
        return entities.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }
}
