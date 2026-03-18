package ma.swiftrent.pattern.observer.rentalstatus;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.service.NotificationServiceFactory;
import org.springframework.stereotype.Component;

// Konkretny observer uruchamia dodatkowy systemowy kanal
// po zmianie statusu wypozyczenia.
@Component
@RequiredArgsConstructor
public class RentalStatusSystemObserver implements RentalStatusChangedObserver {

    private final NotificationServiceFactory notificationServiceFactory;

    @Override
    public void onRentalStatusChanged(RentalStatusChangedEvent event) {
        NotificationComponent notificationComponent = notificationServiceFactory.createNotificationSystem();
        notificationComponent.send("Observer: status wypozyczenia " + event.rentalId()
                + " zmieniono na " + event.currentStatus());
    }
}
