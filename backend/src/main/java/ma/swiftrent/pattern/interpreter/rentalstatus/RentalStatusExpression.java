package ma.swiftrent.pattern.interpreter.rentalstatus;

// Wzorzec Interpreter – Użycie 1: interpretacja statusu wypożyczenia.
// Abstrakcyjne wyrażenie definiuje metodę interpretacji statusu.
public interface RentalStatusExpression {
    boolean interpret(String rentalStatus);
}
