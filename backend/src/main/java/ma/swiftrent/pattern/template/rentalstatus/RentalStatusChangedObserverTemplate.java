package ma.swiftrent.pattern.template.rentalstatus;

import ma.swiftrent.pattern.observer.rentalstatus.RentalStatusChangedEvent;
import ma.swiftrent.pattern.observer.rentalstatus.RentalStatusChangedObserver;

// Tydzień 6, Wzorzec Template 2
// Obsługa zdarzenia zmiany statusu wypożyczenia ma stały szkielet:
// walidacja eventu, przygotowanie treści komunikatu i uruchomienie konkretnej reakcji.
// Podklasy specjalizują tylko createMessage() i handleMessage().
public abstract class RentalStatusChangedObserverTemplate implements RentalStatusChangedObserver {

    @Override
    public final void onRentalStatusChanged(RentalStatusChangedEvent event) {
        validate(event);
        String message = createMessage(event);
        handleMessage(message);
    }

    protected void validate(RentalStatusChangedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Zdarzenie zmiany statusu nie może być nullem");
        }
    }

    protected abstract String createMessage(RentalStatusChangedEvent event);

    protected abstract void handleMessage(String message);
}
// Koniec, Tydzień 6, Wzorzec Template 2
