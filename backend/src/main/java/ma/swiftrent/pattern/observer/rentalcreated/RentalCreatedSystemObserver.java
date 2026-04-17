package ma.swiftrent.pattern.observer.rentalcreated;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.pattern.template.rentalcreated.RentalCreatedObserverTemplate;
import ma.swiftrent.service.NotificationServiceFactory;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 1 – użycie RentalCreatedObserverTemplate (AbstractClass)
@Component
@RequiredArgsConstructor
public class RentalCreatedSystemObserver extends RentalCreatedObserverTemplate {

    private final NotificationServiceFactory notificationServiceFactory;

    @Override
    protected String createMessage(RentalCreatedEvent event) {
        return "Observer: auto " + event.carId()
                + " zostalo wypozyczone przez " + event.userEmail();
    }

    @Override
    protected void handleMessage(String message) {
        NotificationComponent notificationComponent = notificationServiceFactory.createNotificationSystem();
        notificationComponent.send(message);
    }
}
