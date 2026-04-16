package ma.swiftrent.pattern.interpreter.userrole;

// Wzorzec Interpreter – Użycie 3: interpretacja roli użytkownika.
// Abstrakcyjne wyrażenie weryfikujące uprawnienia na podstawie roli.
public interface UserRoleExpression {
    boolean interpret(String userRole);
}
