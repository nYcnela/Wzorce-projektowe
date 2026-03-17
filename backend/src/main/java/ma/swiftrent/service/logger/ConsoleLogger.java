package ma.swiftrent.service.logger;

public class ConsoleLogger implements AppLogger{
    @Override
    public void logInfo(String message) {
        System.out.println("[INFO]: " + message);
    }

    @Override
    public void logError(String message) {
        System.out.println("[ERROR]: " + message);
    }
}
