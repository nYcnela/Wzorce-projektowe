package ma.swiftrent.pattern.bridge.notification;

public class EmailSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("📧 Email: " + message);
    }
}
