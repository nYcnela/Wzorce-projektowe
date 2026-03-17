package ma.swiftrent.composite.notification;

public class EmailNotification implements NotificationComponent {

    @Override
    public void send(String message) {
        System.out.println("📧 Email: " + message);
    }
}
