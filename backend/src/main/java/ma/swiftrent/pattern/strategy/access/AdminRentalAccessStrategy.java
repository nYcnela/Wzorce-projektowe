package ma.swiftrent.pattern.strategy.access;

import ma.swiftrent.entity.Rental;

public final class AdminRentalAccessStrategy implements RentalAccessStrategy {

    @Override
    public void validate(Rental rental, String currentUserEmail) {
        // Administrator ma pełny dostęp do operacji na wypożyczeniu.
    }
}
