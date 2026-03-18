package ma.swiftrent.pattern.observer.rentalstatus;

// Interfejs observera definiuje reakcje
// na zmiane statusu wypozyczenia.
public interface RentalStatusChangedObserver {
    void onRentalStatusChanged(RentalStatusChangedEvent event);
}
