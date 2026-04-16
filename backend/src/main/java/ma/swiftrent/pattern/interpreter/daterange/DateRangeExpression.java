package ma.swiftrent.pattern.interpreter.daterange;

import java.time.LocalDate;

// Wzorzec Interpreter – Użycie 2: interpretacja zakresu dat.
// Abstrakcyjne wyrażenie sprawdzające, czy data mieści się w przedziale.
public interface DateRangeExpression {
    boolean interpret(LocalDate date);
}
