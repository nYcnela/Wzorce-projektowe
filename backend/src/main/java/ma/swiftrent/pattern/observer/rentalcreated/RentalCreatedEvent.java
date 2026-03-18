package ma.swiftrent.pattern.observer.rentalcreated;

import java.math.BigDecimal;

// Event przenosi dane o utworzonym wypozyczeniu.
// Subject przekazuje ten obiekt do wszystkich observerow.
public record RentalCreatedEvent(
        Long rentalId,
        Long carId,
        String userEmail,
        BigDecimal totalCost
) {
}
