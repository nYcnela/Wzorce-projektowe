package ma.swiftrent.pattern.singleton;

import java.time.LocalDate;

// Tydzień 2, Wzorzec Singleton 2 (Bill Pugh)
// Logika wypożyczeń wielokrotnie korzysta z "dzisiejszej" daty.
// Singleton daje jedno, wspólne źródło czasu dla całej aplikacji
// i zastępuje rozproszone wywołania LocalDate.now().
public final class ApplicationClock {

    private ApplicationClock() {
    }

    private static class Holder {
        private static final ApplicationClock INSTANCE = new ApplicationClock();
    }

    public static ApplicationClock getInstance() {
        return Holder.INSTANCE;
    }

    public LocalDate today() {
        return LocalDate.now();
    }
}
// Koniec, Tydzień 2, Wzorzec Singleton 2
