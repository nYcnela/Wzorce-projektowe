package ma.swiftrent.pattern.bridge.notification;

public class SmsSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("📱 SMS: " + message);
    }
}
