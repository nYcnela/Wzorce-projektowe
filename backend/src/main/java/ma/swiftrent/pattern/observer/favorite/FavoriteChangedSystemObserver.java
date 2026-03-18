package ma.swiftrent.pattern.observer.favorite;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.service.NotificationServiceFactory;
import org.springframework.stereotype.Component;

// Konkretny observer uruchamia dodatkowy systemowy kanal
// po zmianie ulubionych.
@Component
@RequiredArgsConstructor
public class FavoriteChangedSystemObserver implements FavoriteChangedObserver {

    private final NotificationServiceFactory notificationServiceFactory;

    @Override
    public void onFavoriteChanged(FavoriteChangedEvent event) {
        NotificationComponent notificationComponent = notificationServiceFactory.createNotificationSystem();
        String action = event.added() ? "dodane do" : "usuniete z";
        notificationComponent.send("Observer: auto " + event.carId()
                + " zostalo " + action + " ulubionych przez " + event.userEmail());
    }
}
