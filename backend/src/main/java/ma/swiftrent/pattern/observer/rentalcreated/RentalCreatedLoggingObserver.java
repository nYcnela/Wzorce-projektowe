package ma.swiftrent.pattern.observer.rentalcreated;

import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import org.springframework.stereotype.Component;

// Konkretny observer reaguje logowaniem
// na utworzenie nowego wypozyczenia.
@Component
public class RentalCreatedLoggingObserver implements RentalCreatedObserver {

    @Override
    public void onRentalCreated(RentalCreatedEvent event) {
        AppLogger logger = new TimestampLoggerDecorator(new ConsoleLogger());
        logger.logInfo("Observer: utworzono wypozyczenie " + event.rentalId()
                + " dla uzytkownika " + event.userEmail());
    }
}
