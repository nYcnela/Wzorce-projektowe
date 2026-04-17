package ma.swiftrent.pattern.observer.favorite;

import ma.swiftrent.pattern.template.favorite.FavoriteChangedObserverTemplate;
import ma.swiftrent.service.notification.BasicNotificationService;
import ma.swiftrent.service.notification.NotificationService;
import ma.swiftrent.service.notification.SmsNotificationDecorator;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 3 – użycie FavoriteChangedObserverTemplate (AbstractClass)
@Component
public class FavoriteChangedNotificationObserver extends FavoriteChangedObserverTemplate {

    @Override
    protected String createMessage(FavoriteChangedEvent event) {
        String action = event.added() ? "dodano do" : "usunieto z";
        return "Observer: auto " + event.carId() + " " + action + " ulubionych";
    }

    @Override
    protected void handleMessage(String message) {
        NotificationService notificationService = new SmsNotificationDecorator(new BasicNotificationService());
        notificationService.send(message);
    }
}
