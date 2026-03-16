package ma.swiftrent.service.logger;

public class LegacyLogger {
    public void writeInfo(String message) {
        System.out.println("[LEGACY INFO] " + message);
    }

    public void writeError(String message) {
        System.out.println("[LEGACY ERROR] " + message);
    }

}
