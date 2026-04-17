package ma.swiftrent.pattern.mediator.rentalbooking;

// Wzorzec Mediator – Użycie 1: koordynacja procesu rezerwacji.
// Interfejs mediatora centralizuje komunikację między komponentami podczas rezerwacji.
public interface RentalBookingMediator {
    void notify(String sender, String event);
}
