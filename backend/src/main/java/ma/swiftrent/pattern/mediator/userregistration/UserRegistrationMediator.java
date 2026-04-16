package ma.swiftrent.pattern.mediator.userregistration;

// Wzorzec Mediator – Użycie 3: koordynacja procesu rejestracji użytkownika.
// Interfejs mediatora centralizuje komunikację między komponentami podczas rejestracji.
public interface UserRegistrationMediator {
    void notify(String sender, String event);
}
