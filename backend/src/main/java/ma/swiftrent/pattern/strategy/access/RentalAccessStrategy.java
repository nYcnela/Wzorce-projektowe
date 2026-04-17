package ma.swiftrent.pattern.strategy.access;

import ma.swiftrent.entity.Rental;

public interface RentalAccessStrategy {

    void validate(Rental rental, String currentUserEmail);
}
