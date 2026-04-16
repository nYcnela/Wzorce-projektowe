package ma.swiftrent.pattern.command.booking;

// Wzorzec Command – Użycie 1: rezerwacja pojazdu.
// Interfejs polecenia enkapsuluje żądanie rezerwacji.
public interface RentalBookingCommand {
    void execute();
}
