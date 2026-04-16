package ma.swiftrent.pattern.command.useremail;

// Wzorzec Command – Użycie 3: zmiana adresu e-mail użytkownika.
// Konkretne polecenie zmieniające adres e-mail użytkownika.
public final class UserEmailChangeCommandImpl implements UserEmailChangeCommand {

    private final String oldEmail;
    private final String newEmail;

    public UserEmailChangeCommandImpl(String oldEmail, String newEmail) {
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
    }

    @Override
    public void execute() {
        System.out.println("[UserEmailChangeCommand] Zmiana e-maila: "
                + oldEmail + " -> " + newEmail);
    }
}
