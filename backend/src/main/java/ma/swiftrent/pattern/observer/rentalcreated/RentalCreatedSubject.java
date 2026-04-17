package ma.swiftrent.pattern.observer.rentalcreated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// Tydzień 6, Wzorzec Observer 1
// Po utworzeniu wypożyczenia RentalService publikuje zdarzenie RentalCreatedEvent.
// Subject utrzymuje listę observerów odpowiedzialnych za logowanie i powiadomienia
// i rozsyła do nich wspólne zdarzenie bez wiązania serwisu z konkretnymi reakcjami.
@Component
@RequiredArgsConstructor
public class RentalCreatedSubject {

    private final List<RentalCreatedObserver> observers;

    public void notifyObservers(RentalCreatedEvent event) {
        observers.forEach(observer -> observer.onRentalCreated(event));
    }

}
// Koniec, Tydzień 6, Wzorzec Observer 1
