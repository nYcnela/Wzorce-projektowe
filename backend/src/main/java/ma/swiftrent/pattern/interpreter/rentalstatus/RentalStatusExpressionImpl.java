package ma.swiftrent.pattern.interpreter.rentalstatus;

// Wzorzec Interpreter – Użycie 1: interpretacja statusu wypożyczenia.
// Wyrażenie terminalne sprawdzające, czy status wypożyczenia jest aktywny.
public final class RentalStatusExpressionImpl implements RentalStatusExpression {

    private final String expectedStatus;

    public RentalStatusExpressionImpl(String expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    @Override
    public boolean interpret(String rentalStatus) {
        return expectedStatus.equalsIgnoreCase(rentalStatus);
    }
}
