package ma.swiftrent.pattern.observer.rentalstatus;

import ma.swiftrent.entity.Rental;

// Event przenosi dane o zmianie statusu wypozyczenia,
// lacznie z poprzednim i nowym stanem.
public record RentalStatusChangedEvent(
        Long rentalId,
        Long carId,
        String userEmail,
        Rental.RentalStatus previousStatus,
        Rental.RentalStatus currentStatus
) {
}
