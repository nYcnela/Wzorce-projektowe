package ma.swiftrent.service.notification;

/*
    Tydzień 3, Wzorzec Decorator 3
    Dekoratory do wysyłąnia wiadomości poprzez email i sms
 */
public abstract class NotificationDecorator implements NotificationService {

    protected final NotificationService notificationService;

    public NotificationDecorator(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
//Koniec Tydzień 3, Wzorzec Decorator 3
