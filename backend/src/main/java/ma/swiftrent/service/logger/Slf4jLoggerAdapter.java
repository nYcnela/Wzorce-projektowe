package ma.swiftrent.service.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    Tydzień 3, Wzorzec Adapter 1
    Adapter pomiędzy loggerem w naszym projekcie, a loggerem Slf4j
    Implementacja w formie kompozycji
 */
public class Slf4jLoggerAdapter implements AppLogger {

    private final Logger logger;

    public Slf4jLoggerAdapter(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void logInfo(String message) {
        logger.info(message);
    }

    @Override
    public void logError(String message) {
        logger.error(message);
    }
}
//Koniec Tydzień 3, Wzorzec Adapter 2