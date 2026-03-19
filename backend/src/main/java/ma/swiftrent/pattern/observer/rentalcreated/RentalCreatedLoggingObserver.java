package ma.swiftrent.pattern.observer.rentalcreated;

import ma.swiftrent.pattern.template.rentalcreated.RentalCreatedObserverTemplate;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 1 – użycie RentalCreatedObserverTemplate (AbstractClass)
@Component
public class RentalCreatedLoggingObserver extends RentalCreatedObserverTemplate {

    @Override
    protected String createMessage(RentalCreatedEvent event) {
        return "Observer: utworzono wypozyczenie " + event.rentalId()
                + " dla uzytkownika " + event.userEmail();
    }

    @Override
    protected void handleMessage(String message) {
        AppLogger logger = new TimestampLoggerDecorator(new ConsoleLogger());
        logger.logInfo(message);
    }
}
