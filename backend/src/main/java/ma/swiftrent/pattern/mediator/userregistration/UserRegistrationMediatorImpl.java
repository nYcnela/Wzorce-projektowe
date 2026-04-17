package ma.swiftrent.pattern.mediator.userregistration;

// Wzorzec Mediator – Użycie 3: koordynacja procesu rejestracji użytkownika.
// Konkretny mediator koordynujący walidację danych i wysyłkę e-maila powitalnego.
public final class UserRegistrationMediatorImpl implements UserRegistrationMediator {

    @Override
    public void notify(String sender, String event) {
        if ("ValidationService".equals(sender) && "DATA_VALID".equals(event)) {
            System.out.println("[UserRegistrationMediator] Dane poprawne – tworzę konto użytkownika.");
        } else if ("AccountService".equals(sender) && "ACCOUNT_CREATED".equals(event)) {
            System.out.println("[UserRegistrationMediator] Konto założone – wysyłam e-mail powitalny.");
        } else {
            System.out.println("[UserRegistrationMediator] Zdarzenie od " + sender + ": " + event);
        }
    }
}
