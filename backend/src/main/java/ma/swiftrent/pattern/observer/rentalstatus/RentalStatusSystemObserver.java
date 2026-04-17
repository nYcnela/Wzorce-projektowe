package ma.swiftrent.pattern.observer.rentalstatus;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.pattern.template.rentalstatus.RentalStatusChangedObserverTemplate;
import ma.swiftrent.service.NotificationServiceFactory;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 2 – użycie RentalStatusChangedObserverTemplate (AbstractClass)
@Component
@RequiredArgsConstructor
public class RentalStatusSystemObserver extends RentalStatusChangedObserverTemplate {

    private final NotificationServiceFactory notificationServiceFactory;

    @Override
    protected String createMessage(RentalStatusChangedEvent event) {
        return "Observer: status wypozyczenia " + event.rentalId()
                + " zmieniono na " + event.currentStatus();
    }

    @Override
    protected void handleMessage(String message) {
        NotificationComponent notificationComponent = notificationServiceFactory.createNotificationSystem();
        notificationComponent.send(message);
    }
}
