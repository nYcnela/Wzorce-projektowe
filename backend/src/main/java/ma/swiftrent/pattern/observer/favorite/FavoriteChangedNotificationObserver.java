package ma.swiftrent.pattern.observer.favorite;

import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.NotificationService;
import ma.swiftrent.service.notification.SmsNotificationDecorator;
import org.springframework.stereotype.Component;

// Konkretny observer wysyla powiadomienie
// po zmianie ulubionych.
@Component
public class FavoriteChangedNotificationObserver implements FavoriteChangedObserver {

    @Override
    public void onFavoriteChanged(FavoriteChangedEvent event) {
        NotificationService notificationService = new SmsNotificationDecorator(new BasicNotificationService());
        String action = event.added() ? "dodano do" : "usunieto z";
        notificationService.send("Observer: auto " + event.carId() + " " + action + " ulubionych");
    }
}
