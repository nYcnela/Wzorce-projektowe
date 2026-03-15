package ma.swiftrent.exception.flyweight;

import ma.swiftrent.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.Map;

// Tydzien 4, Wzorzec Flyweight 2
// Odpowiedzi bledow powtarzaja stale elementy, takie jak status i tytul bledu.
// Flyweight wspoldzieli szablon odpowiedzi, a dane zewnetrzne jak timestamp czy message sa przekazywane przy uzyciu.
public final class ErrorTemplateFlyweight {

    private final int status;
    private final String error;
    private final String defaultMessage;

    ErrorTemplateFlyweight(int status, String error, String defaultMessage) {
        this.status = status;
        this.error = error;
        this.defaultMessage = defaultMessage;
    }

    public int getStatus() {
        return status;
    }

    public ErrorResponse toResponse(
            LocalDateTime timestamp,
            String message,
            Map<String, String> validationErrors
    ) {
        return ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message == null ? defaultMessage : message)
                .validationErrors(validationErrors)
                .build();
    }
}
// Koniec, Tydzien 4, Wzorzec Flyweight 2
