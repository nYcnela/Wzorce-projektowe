package ma.swiftrent.pattern.state.rental;

import ma.swiftrent.entity.Rental;
import ma.swiftrent.pattern.singleton.ApplicationClock;

public interface RentalState {

    void returnRental(Rental rental, ApplicationClock clock);

    void cancelRental(Rental rental, ApplicationClock clock);

    boolean blocksAvailability();
}
