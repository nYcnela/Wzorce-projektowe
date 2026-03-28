package ma.swiftrent.service.notification;

/*
    Tydzień 3, Wzorzec Decorator 3
    Dekoratory do wysyłąnia wiadomości poprzez email i sms

    Tydzień 7, Zasada otwarte-zamknięte 1
    Jako dekorator ta klasa spełnia OCP,
    jeżeli chcemy dodać nową funkcjonalność to po prostu
    robimy nowy dekorator implementujący interfejs NotificationService
    i nie trzeba zmieniać nic w BasicNotificationService
 */
public abstract class NotificationDecorator implements NotificationService {

    protected final NotificationService notificationService;

    public NotificationDecorator(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
//Koniec Tydzień 3, Wzorzec Decorator 3
//Koniec Tydzień 7, Zasada otwarte-zamknięte 1
