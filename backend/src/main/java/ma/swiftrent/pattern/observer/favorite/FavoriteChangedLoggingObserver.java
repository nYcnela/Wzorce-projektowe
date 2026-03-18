package ma.swiftrent.pattern.observer.favorite;

import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import org.springframework.stereotype.Component;

// Konkretny observer loguje dodanie lub usuniecie auta
// z listy ulubionych.
@Component
public class FavoriteChangedLoggingObserver implements FavoriteChangedObserver {

    @Override
    public void onFavoriteChanged(FavoriteChangedEvent event) {
        AppLogger logger = new TimestampLoggerDecorator(new ConsoleLogger());
        String action = event.added() ? "dodano do" : "usunieto z";
        logger.logInfo("Observer: uzytkownik " + event.userEmail()
                + " " + action + " ulubionych auto " + event.carId());
    }
}
