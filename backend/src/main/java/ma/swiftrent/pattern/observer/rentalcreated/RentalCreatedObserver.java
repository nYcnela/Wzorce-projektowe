package ma.swiftrent.pattern.observer.rentalcreated;

// Interfejs observera definiuje wspolna reakcje
// na utworzenie nowego wypozyczenia.
public interface RentalCreatedObserver {
    void onRentalCreated(RentalCreatedEvent event);
}
