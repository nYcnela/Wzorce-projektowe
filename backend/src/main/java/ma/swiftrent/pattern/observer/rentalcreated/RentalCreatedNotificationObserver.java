package ma.swiftrent.pattern.observer.rentalcreated;

import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.EmailNotificationDecorator;
import ma.swiftrent.service.notification.NotificationService;
import ma.swiftrent.service.notification.SmsNotificationDecorator;
import org.springframework.stereotype.Component;

// Konkretny observer wysyla powiadomienie do uzytkownika
// po utworzeniu wypozyczenia.
@Component
public class RentalCreatedNotificationObserver implements RentalCreatedObserver {

    @Override
    public void onRentalCreated(RentalCreatedEvent event) {
        NotificationService notificationService = new BasicNotificationService();
        notificationService = new EmailNotificationDecorator(notificationService);
        notificationService = new SmsNotificationDecorator(notificationService);
        notificationService.send("Observer: zarejestrowano wypozyczenie " + event.rentalId()
                + " na kwote " + event.totalCost());
    }
}
