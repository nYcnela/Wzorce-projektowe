package ma.swiftrent.pattern.command.useremail;

// Wzorzec Command – Użycie 3: zmiana adresu e-mail użytkownika.
// Interfejs polecenia enkapsuluje żądanie zmiany e-maila.
public interface UserEmailChangeCommand {
    void execute();
}
