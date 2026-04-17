package ma.swiftrent.pattern.strategy.access;

import ma.swiftrent.entity.Rental;

public final class OwnerRentalAccessStrategy implements RentalAccessStrategy {

    @Override
    public void validate(Rental rental, String currentUserEmail) {
        if (rental.getUser() == null || !rental.getUser().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Nie masz uprawnień do tej operacji na wypożyczeniu");
        }
    }
}
