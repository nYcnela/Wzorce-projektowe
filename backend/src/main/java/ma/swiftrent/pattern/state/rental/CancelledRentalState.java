package ma.swiftrent.pattern.state.rental;

import ma.swiftrent.entity.Rental;
import ma.swiftrent.pattern.singleton.ApplicationClock;

public final class CancelledRentalState implements RentalState {

    @Override
    public void returnRental(Rental rental, ApplicationClock clock) {
        throw new RuntimeException("Wypożyczenie nie jest aktywne");
    }

    @Override
    public void cancelRental(Rental rental, ApplicationClock clock) {
        throw new RuntimeException("Tylko aktywne rezerwacje można anulować");
    }

    @Override
    public boolean blocksAvailability() {
        return false;
    }
}
