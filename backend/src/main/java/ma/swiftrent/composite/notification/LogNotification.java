package ma.swiftrent.composite.notification;

public class LogNotification implements NotificationComponent {

    @Override
    public void send(String message) {
        System.out.println("📝 LOG: " + message);
    }
}
