package ma.swiftrent.pattern.command.carstatus;

// Wzorzec Command – Użycie 2: zmiana statusu pojazdu.
// Konkretne polecenie aktualizujące status pojazdu w systemie.
public final class CarStatusUpdateCommandImpl implements CarStatusUpdateCommand {

    private final String carPlate;
    private final String newStatus;

    public CarStatusUpdateCommandImpl(String carPlate, String newStatus) {
        this.carPlate = carPlate;
        this.newStatus = newStatus;
    }

    @Override
    public void execute() {
        System.out.println("[CarStatusUpdateCommand] Auto " + carPlate
                + " -> nowy status: " + newStatus);
    }
}
