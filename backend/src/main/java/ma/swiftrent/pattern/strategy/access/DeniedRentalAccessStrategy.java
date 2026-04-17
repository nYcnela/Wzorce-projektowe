package ma.swiftrent.pattern.strategy.access;

import ma.swiftrent.entity.Rental;

public final class DeniedRentalAccessStrategy implements RentalAccessStrategy {

    @Override
    public void validate(Rental rental, String currentUserEmail) {
        throw new RuntimeException("Nie masz uprawnień do tej operacji na wypożyczeniu");
    }
}
