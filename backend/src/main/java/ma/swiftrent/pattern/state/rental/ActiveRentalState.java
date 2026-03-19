package ma.swiftrent.pattern.state.rental;

import ma.swiftrent.entity.Rental;
import ma.swiftrent.pattern.factory.CancelRentalActionFactory;
import ma.swiftrent.pattern.factory.ReturnRentalActionFactory;
import ma.swiftrent.pattern.singleton.ApplicationClock;

public final class ActiveRentalState implements RentalState {

    @Override
    public void returnRental(Rental rental, ApplicationClock clock) {
        new ReturnRentalActionFactory(clock).process(rental);
    }

    @Override
    public void cancelRental(Rental rental, ApplicationClock clock) {
        if (!rental.getStartDate().isAfter(clock.today())) {
            throw new RuntimeException("Nie można anulować wypożyczenia, które już się rozpoczęło. Użyj opcji zwrotu.");
        }

        new CancelRentalActionFactory().process(rental);
    }

    @Override
    public boolean blocksAvailability() {
        return true;
    }
}
