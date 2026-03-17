package ma.swiftrent.service.notification;

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
