package ma.swiftrent.pattern.command.carstatus;

// Wzorzec Command – Użycie 2: zmiana statusu pojazdu.
// Interfejs polecenia enkapsuluje żądanie zmiany statusu auta.
public interface CarStatusUpdateCommand {
    void execute();
}
