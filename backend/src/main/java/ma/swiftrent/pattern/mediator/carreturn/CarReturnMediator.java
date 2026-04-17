package ma.swiftrent.pattern.mediator.carreturn;

// Wzorzec Mediator – Użycie 2: koordynacja procesu zwrotu pojazdu.
// Interfejs mediatora centralizuje komunikację między komponentami podczas zwrotu.
public interface CarReturnMediator {
    void notify(String sender, String event);
}
