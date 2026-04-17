package ma.swiftrent.pattern.bridge.notification;

public class UserNotification extends Notification {

    public UserNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.send("👤 User: " + message);
    }
}
