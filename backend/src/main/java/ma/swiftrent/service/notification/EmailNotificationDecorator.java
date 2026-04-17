package ma.swiftrent.service.notification;

/*
    Tydzień 7, Zasada pojedyńczej odpowiedzialności 2
    Wysyłanie powiadomień jest rozbite na wiele klas, dzięki czemu
    zmiany zasad wysyłąnia emaili nie wpływają na klasę powiadomień sms,
    a więc spełnione jest założenie pojedyńczej odpowiedzialności
 */
public class EmailNotificationDecorator extends NotificationDecorator {

    public EmailNotificationDecorator(NotificationService notificationService) {
        super(notificationService);
    }

    @Override
    public void send(String message) {
        notificationService.send(message);
        sendEmail(message);
    }

    private void sendEmail(String message) {
        System.out.println("Email wysłany: " + message);
    }
}
//Koniec Tydzień 7, Zasad pojedyńczej odpowiedzialności 2
