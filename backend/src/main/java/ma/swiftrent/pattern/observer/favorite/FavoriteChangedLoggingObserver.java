package ma.swiftrent.pattern.observer.favorite;

import ma.swiftrent.pattern.template.favorite.FavoriteChangedObserverTemplate;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 3 – użycie FavoriteChangedObserverTemplate (AbstractClass)
@Component
public class FavoriteChangedLoggingObserver extends FavoriteChangedObserverTemplate {

    @Override
    protected String createMessage(FavoriteChangedEvent event) {
        String action = event.added() ? "dodano do" : "usunieto z";
        return "Observer: uzytkownik " + event.userEmail()
                + " " + action + " ulubionych auto " + event.carId();
    }

    @Override
    protected void handleMessage(String message) {
        AppLogger logger = new TimestampLoggerDecorator(new ConsoleLogger());
        logger.logInfo(message);
    }
}
