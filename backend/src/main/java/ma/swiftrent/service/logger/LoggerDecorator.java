package ma.swiftrent.service.logger;

/*
    Tydzień 3, Wzorzec Dekorator 2
    Dekorator do loggera
    Stworzono 2 nakładki: logger z czasem i logger od ochrony
 */
public abstract class LoggerDecorator implements AppLogger {

    protected final AppLogger logger;

    public LoggerDecorator(AppLogger logger) {
        this.logger = logger;
    }
}
