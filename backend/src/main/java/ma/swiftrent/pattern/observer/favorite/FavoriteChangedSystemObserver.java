package ma.swiftrent.pattern.observer.favorite;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.composite.notification.NotificationComponent;
import ma.swiftrent.pattern.template.favorite.FavoriteChangedObserverTemplate;
import ma.swiftrent.service.NotificationServiceFactory;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 3 – użycie FavoriteChangedObserverTemplate (AbstractClass)
@Component
@RequiredArgsConstructor
public class FavoriteChangedSystemObserver extends FavoriteChangedObserverTemplate {

    private final NotificationServiceFactory notificationServiceFactory;

    @Override
    protected String createMessage(FavoriteChangedEvent event) {
        String action = event.added() ? "dodane do" : "usuniete z";
        return "Observer: auto " + event.carId()
                + " zostalo " + action + " ulubionych przez " + event.userEmail();
    }

    @Override
    protected void handleMessage(String message) {
        NotificationComponent notificationComponent = notificationServiceFactory.createNotificationSystem();
        notificationComponent.send(message);
    }
}
