package ma.swiftrent.pattern.interpreter.userrole;

// Wzorzec Interpreter – Użycie 3: interpretacja roli użytkownika.
// Wyrażenie terminalne sprawdzające, czy użytkownik posiada wymaganą rolę.
public final class UserRoleExpressionImpl implements UserRoleExpression {

    private final String requiredRole;

    public UserRoleExpressionImpl(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean interpret(String userRole) {
        return requiredRole.equalsIgnoreCase(userRole);
    }
}
