package ma.swiftrent.pattern.observer.rentalstatus;

import ma.swiftrent.pattern.template.rentalstatus.RentalStatusChangedObserverTemplate;
import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import org.springframework.stereotype.Component;

// Tydzień 6, Wzorzec Template 2 – użycie RentalStatusChangedObserverTemplate (AbstractClass)
@Component
public class RentalStatusLoggingObserver extends RentalStatusChangedObserverTemplate {

    @Override
    protected String createMessage(RentalStatusChangedEvent event) {
        return "Observer: wypozyczenie " + event.rentalId()
                + " zmienilo status z " + event.previousStatus()
                + " na " + event.currentStatus();
    }

    @Override
    protected void handleMessage(String message) {
        AppLogger logger = new TimestampLoggerDecorator(new ConsoleLogger());
        logger.logInfo(message);
    }
}
