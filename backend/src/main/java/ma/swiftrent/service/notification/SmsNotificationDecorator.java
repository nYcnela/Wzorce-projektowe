package ma.swiftrent.service.notification;

public class SmsNotificationDecorator extends NotificationDecorator {

    public SmsNotificationDecorator(NotificationService notificationService) {
        super(notificationService);
    }

    @Override
    public void send(String message) {
        notificationService.send(message);
        sendSms(message);
    }

    private void sendSms(String message) {
        System.out.println("SMS wysłany: " + message);
    }
}
