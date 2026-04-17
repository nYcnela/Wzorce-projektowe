package ma.swiftrent.pattern.observer.rentalstatus;

import ma.swiftrent.pattern.template.rentalstatus.RentalStatusChangedObserverTemplate;
import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.EmailNotificationDecorator;
import ma.swiftrent.service.notification.NotificationService;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 2 – użycie RentalStatusChangedObserverTemplate (AbstractClass)
@Component
public class RentalStatusNotificationObserver extends RentalStatusChangedObserverTemplate {

    @Override
    protected String createMessage(RentalStatusChangedEvent event) {
        return "Observer: wypozyczenie " + event.rentalId()
                + " ma teraz status " + event.currentStatus();
    }

    @Override
    protected void handleMessage(String message) {
        NotificationService notificationService = new EmailNotificationDecorator(new BasicNotificationService());
        notificationService.send(message);
    }
}
