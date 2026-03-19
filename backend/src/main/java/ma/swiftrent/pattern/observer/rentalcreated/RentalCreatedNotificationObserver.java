package ma.swiftrent.pattern.observer.rentalcreated;

import ma.swiftrent.pattern.template.rentalcreated.RentalCreatedObserverTemplate;
import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.EmailNotificationDecorator;
import ma.swiftrent.service.notification.NotificationService;
import ma.swiftrent.service.notification.SmsNotificationDecorator;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 1 – użycie RentalCreatedObserverTemplate (AbstractClass)
@Component
public class RentalCreatedNotificationObserver extends RentalCreatedObserverTemplate {

    @Override
    protected String createMessage(RentalCreatedEvent event) {
        return "Observer: zarejestrowano wypozyczenie " + event.rentalId()
                + " na kwote " + event.totalCost();
    }

    @Override
    protected void handleMessage(String message) {
        NotificationService notificationService = new BasicNotificationService();
        notificationService = new EmailNotificationDecorator(notificationService);
        notificationService = new SmsNotificationDecorator(notificationService);
        notificationService.send(message);
    }
}
