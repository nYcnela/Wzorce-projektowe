package ma.swiftrent.composite.notification;

public class SmsNotification implements NotificationComponent {

    @Override
    public void send(String message) {
        System.out.println("📱 SMS: " + message);
    }
}
