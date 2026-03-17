package ma.swiftrent.service.logger;

import java.time.LocalDateTime;

public class TimestampLoggerDecorator extends LoggerDecorator {

    public TimestampLoggerDecorator(AppLogger logger) {
        super(logger);
    }

    @Override
    public void logInfo(String message) {
        String newMessage = LocalDateTime.now() + " | " + message;
        logger.logInfo(newMessage);
    }

    @Override
    public void logError(String message) {
        String newMessage = LocalDateTime.now() + " | " + message;
        logger.logError(newMessage);
    }
}
