package ma.swiftrent.pattern.observer.rentalstatus;

import ma.swiftrent.service.logger.AppLogger;
import ma.swiftrent.service.logger.ConsoleLogger;
import ma.swiftrent.service.logger.TimestampLoggerDecorator;
import org.springframework.stereotype.Component;

// Konkretny observer loguje zmiane statusu
// wypozyczenia.
@Component
public class RentalStatusLoggingObserver implements RentalStatusChangedObserver {

    @Override
    public void onRentalStatusChanged(RentalStatusChangedEvent event) {
        AppLogger logger = new TimestampLoggerDecorator(new ConsoleLogger());
        logger.logInfo("Observer: wypozyczenie " + event.rentalId()
                + " zmienilo status z " + event.previousStatus()
                + " na " + event.currentStatus());
    }
}
