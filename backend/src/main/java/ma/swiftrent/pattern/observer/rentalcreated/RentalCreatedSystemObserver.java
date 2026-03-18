package ma.swiftrent.pattern.observer.rentalcreated;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.service.NotificationServiceFactory;
import org.springframework.stereotype.Component;

// Konkretny observer uruchamia dodatkowy systemowy kanal
// powiadomien po utworzeniu wypozyczenia.
@Component
@RequiredArgsConstructor
public class RentalCreatedSystemObserver implements RentalCreatedObserver {

    private final NotificationServiceFactory notificationServiceFactory;

    @Override
    public void onRentalCreated(RentalCreatedEvent event) {
        NotificationComponent notificationComponent = notificationServiceFactory.createNotificationSystem();
        notificationComponent.send("Observer: auto " + event.carId()
                + " zostalo wypozyczone przez " + event.userEmail());
    }
}
