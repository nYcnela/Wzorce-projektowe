package ma.swiftrent.service.notification;

public class BasicNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Powiadomienie: " + message);
    }
}
