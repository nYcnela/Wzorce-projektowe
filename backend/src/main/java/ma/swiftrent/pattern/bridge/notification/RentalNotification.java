package ma.swiftrent.pattern.bridge.notification;

public class RentalNotification extends Notification {

    public RentalNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void send(String message) {
        sender.send("🚗 Rental: " + message);
    }
}
