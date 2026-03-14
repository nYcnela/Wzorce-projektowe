package ma.swiftrent.exception;

import ma.swiftrent.exception.flyweight.ErrorTemplateFactory;
import ma.swiftrent.exception.flyweight.ErrorTemplateFlyweight;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Globalny handler wyjątków dla całej aplikacji.
 * Przechwytuje i formatuje błędy do czytelnych odpowiedzi JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Obsługuje błędy walidacji (Bean Validation).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println("Wystapil bład walidacji");
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            System.out.println("Pole: " + fieldName + " - " + errorMessage);
            errors.put(fieldName, errorMessage);
        });
        return buildResponse(ErrorTemplateFactory.validation(), null, errors);
    }

    /**
     * Obsługuje błędy uwierzytelniania (złe hasło).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(ErrorTemplateFactory.badCredentials(), null, null);
    }

    /**
     * Obsługuje błąd "użytkownik nie znaleziony".
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {
        return buildResponse(ErrorTemplateFactory.userNotFound(), ex.getMessage(), null);
    }

    /**
     * Obsługuje ogólne wyjątki RuntimeException (np. z serwisów).
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return buildResponse(ErrorTemplateFactory.runtimeError(), ex.getMessage(), null);
    }

    /**
     * Obsługuje wszystkie nieobsłużone wyjątki.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        System.out.println("Błąd: " + ex.getMessage());
        ex.printStackTrace();
        return buildResponse(ErrorTemplateFactory.internalError(), "Szczegoly bledu: " + ex.getMessage(), null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            ErrorTemplateFlyweight template,
            String message,
            Map<String, String> validationErrors
    ) {
        ErrorResponse errorResponse = template.toResponse(LocalDateTime.now(), message, validationErrors);
        return ResponseEntity.status(template.getStatus()).body(errorResponse);
    }
}
