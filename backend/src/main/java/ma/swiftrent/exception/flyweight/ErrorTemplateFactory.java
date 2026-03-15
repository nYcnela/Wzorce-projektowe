package ma.swiftrent.exception.flyweight;

import org.springframework.http.HttpStatus;

public final class ErrorTemplateFactory {

    private static final ErrorTemplateFlyweight VALIDATION_TEMPLATE = new ErrorTemplateFlyweight(
            HttpStatus.BAD_REQUEST.value(),
            "Blad walidacji",
            "Dane wejsciowe sa nieprawidlowe"
    );
    private static final ErrorTemplateFlyweight BAD_CREDENTIALS_TEMPLATE = new ErrorTemplateFlyweight(
            HttpStatus.UNAUTHORIZED.value(),
            "Nieprawidlowe dane logowania",
            "Email lub haslo jest nieprawidlowe"
    );
    private static final ErrorTemplateFlyweight USER_NOT_FOUND_TEMPLATE = new ErrorTemplateFlyweight(
            HttpStatus.NOT_FOUND.value(),
            "Uzytkownik nie zostal znaleziony",
            "Nie znaleziono uzytkownika"
    );
    private static final ErrorTemplateFlyweight RUNTIME_TEMPLATE = new ErrorTemplateFlyweight(
            HttpStatus.BAD_REQUEST.value(),
            "Blad operacji",
            "Nie mozna wykonac operacji"
    );
    private static final ErrorTemplateFlyweight INTERNAL_TEMPLATE = new ErrorTemplateFlyweight(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Wewnetrzny blad serwera",
            "Wystapil nieoczekiwany blad"
    );

    private ErrorTemplateFactory() {
    }

    public static ErrorTemplateFlyweight validation() {
        return VALIDATION_TEMPLATE;
    }

    public static ErrorTemplateFlyweight badCredentials() {
        return BAD_CREDENTIALS_TEMPLATE;
    }

    public static ErrorTemplateFlyweight userNotFound() {
        return USER_NOT_FOUND_TEMPLATE;
    }

    public static ErrorTemplateFlyweight runtimeError() {
        return RUNTIME_TEMPLATE;
    }

    public static ErrorTemplateFlyweight internalError() {
        return INTERNAL_TEMPLATE;
    }
}
