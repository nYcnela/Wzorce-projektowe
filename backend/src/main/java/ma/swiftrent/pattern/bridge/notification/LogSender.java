package ma.swiftrent.pattern.bridge.notification;

public class LogSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("📝 LOG: " + message);
    }
}
