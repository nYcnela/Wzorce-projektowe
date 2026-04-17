package ma.swiftrent.pattern.state.rental;

import ma.swiftrent.entity.Rental;

// Tydzień 6, Wzorzec State 1
// Zachowanie wypożyczenia zależy od aktualnego statusu ACTIVE, COMPLETED lub CANCELLED.
// Kontekst wybiera odpowiedni obiekt stanu i deleguje do niego operacje zwrotu, anulowania
// oraz decyzję, czy rezerwacja nadal blokuje dostępność samochodu.
public final class RentalStateContext {

    private final RentalState activeState = new ActiveRentalState();
    private final RentalState completedState = new CompletedRentalState();
    private final RentalState cancelledState = new CancelledRentalState();

    public RentalState resolve(Rental rental) {
        return switch (rental.getStatus()) {
            case ACTIVE -> activeState;
            case COMPLETED -> completedState;
            case CANCELLED -> cancelledState;
        };
    }
}
// Koniec, Tydzień 6, Wzorzec State 1
