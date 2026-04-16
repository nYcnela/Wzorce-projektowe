package ma.swiftrent.pattern.memento.user;

// Wzorzec Memento – Użycie 3: zapis preferencji użytkownika.
// Interfejs pamiątki przechowuje migawkę preferencji użytkownika.
public interface UserPreferencesMemento {
    String getSavedLanguage();
    String getSavedCurrency();
    boolean getSavedNewsletterEnabled();
}
