package ma.swiftrent.pattern.observer.rentalstatus;

import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.EmailNotificationDecorator;
import ma.swiftrent.service.notification.NotificationService;
import org.springframework.stereotype.Component;

// Konkretny observer wysyla powiadomienie,
// gdy status wypozyczenia ulega zmianie.
@Component
public class RentalStatusNotificationObserver implements RentalStatusChangedObserver {

    @Override
    public void onRentalStatusChanged(RentalStatusChangedEvent event) {
        NotificationService notificationService = new EmailNotificationDecorator(new BasicNotificationService());
        notificationService.send("Observer: wypozyczenie " + event.rentalId()
                + " ma teraz status " + event.currentStatus());
    }
}
