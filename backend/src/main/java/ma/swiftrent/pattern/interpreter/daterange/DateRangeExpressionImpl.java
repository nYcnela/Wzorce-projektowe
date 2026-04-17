package ma.swiftrent.pattern.interpreter.daterange;

import java.time.LocalDate;

// Wzorzec Interpreter – Użycie 2: interpretacja zakresu dat.
// Wyrażenie terminalne weryfikujące, czy data wypożyczenia mieści się w przedziale.
public final class DateRangeExpressionImpl implements DateRangeExpression {

    private final LocalDate from;
    private final LocalDate to;

    public DateRangeExpressionImpl(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean interpret(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }
}
