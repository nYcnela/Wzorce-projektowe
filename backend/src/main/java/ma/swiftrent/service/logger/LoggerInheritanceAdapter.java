package ma.swiftrent.service.logger;

/*
    Tydzień 2, Wzorzec Adapter 2
    Adapter pomiędzy nowym Loggerem, a starym Loggerem
    Implementacja poprzez dziedziczenie
 */
public class LoggerInheritanceAdapter extends LegacyLogger implements AppLogger {

    @Override
    public void logInfo(String message) {
        writeInfo(message);
    }

    @Override
    public void logError(String message) {
        writeError(message);
    }

}
//Koniec Tydzień 2, Wzorzec Adapter 2
