package ma.swiftrent.pattern.command.booking;

// Wzorzec Command – Użycie 1: rezerwacja pojazdu.
// Konkretne polecenie rezerwujące pojazd dla użytkownika.
public final class RentalBookingCommandImpl implements RentalBookingCommand {

    private final String carPlate;
    private final String userEmail;

    public RentalBookingCommandImpl(String carPlate, String userEmail) {
        this.carPlate = carPlate;
        this.userEmail = userEmail;
    }

    @Override
    public void execute() {
        System.out.println("[RentalBookingCommand] Rezerwacja auta " + carPlate
                + " dla uzytkownika " + userEmail);
    }
}
