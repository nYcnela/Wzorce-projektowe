package ma.swiftrent.pattern.observer.rentalstatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// Tydzień 6, Wzorzec Observer 2
// Zmiana statusu wypożyczenia po zwrocie lub anulowaniu tworzy zdarzenie RentalStatusChangedEvent.
// Subject przekazuje je do observerów, którzy niezależnie wykonują logowanie
// oraz reakcje powiadomień bez rozbudowywania logiki RentalService.
@Component
@RequiredArgsConstructor
public class RentalStatusChangedSubject {

    private final List<RentalStatusChangedObserver> observers;

    public void notifyObservers(RentalStatusChangedEvent event) {
        observers.forEach(observer -> observer.onRentalStatusChanged(event));
    }

}
// Koniec, Tydzień 6, Wzorzec Observer 2
