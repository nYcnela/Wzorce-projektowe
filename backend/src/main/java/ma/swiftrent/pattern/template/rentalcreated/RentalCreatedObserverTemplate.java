package ma.swiftrent.pattern.template.rentalcreated;

import ma.swiftrent.pattern.observer.rentalcreated.RentalCreatedEvent;
import ma.swiftrent.pattern.observer.rentalcreated.RentalCreatedObserver;

// Tydzień 6, Wzorzec Template 1
// Obsługa zdarzenia utworzenia wypożyczenia przebiega zawsze według tego samego schematu:
// walidacja eventu, zbudowanie komunikatu i wykonanie konkretnej reakcji.
// Podklasy nadpisują tylko kroki createMessage() i handleMessage().
public abstract class RentalCreatedObserverTemplate implements RentalCreatedObserver {

    @Override
    public final void onRentalCreated(RentalCreatedEvent event) {
        validate(event);
        String message = createMessage(event);
        handleMessage(message);
    }

    protected void validate(RentalCreatedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Zdarzenie utworzenia wypożyczenia nie może być nullem");
        }
    }

    protected abstract String createMessage(RentalCreatedEvent event);

    protected abstract void handleMessage(String message);
}
// Koniec, Tydzień 6, Wzorzec Template 1
