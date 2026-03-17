package ma.swiftrent.service.logger;

public class SecurityLoggerDecorator extends LoggerDecorator {

    public SecurityLoggerDecorator(AppLogger logger) {
        super(logger);
    }

    @Override
    public void logInfo(String message) {
        String newMessage = "[SECURITY] " + message;
        logger.logInfo(newMessage);
    }

    @Override
    public void logError(String message) {
        String newMessage = "[SECURITY] " + message;
        logger.logError(newMessage);
    }
}
