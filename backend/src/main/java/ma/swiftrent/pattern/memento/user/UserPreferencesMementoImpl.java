package ma.swiftrent.pattern.memento.user;

// Wzorzec Memento – Użycie 3: zapis preferencji użytkownika.
// Konkretna pamiątka przechowująca migawkę języka, waluty i zgody na newsletter.
public final class UserPreferencesMementoImpl implements UserPreferencesMemento {

    private final String savedLanguage;
    private final String savedCurrency;
    private final boolean savedNewsletterEnabled;

    public UserPreferencesMementoImpl(String savedLanguage, String savedCurrency, boolean savedNewsletterEnabled) {
        this.savedLanguage = savedLanguage;
        this.savedCurrency = savedCurrency;
        this.savedNewsletterEnabled = savedNewsletterEnabled;
    }

    @Override
    public String getSavedLanguage() {
        return savedLanguage;
    }

    @Override
    public String getSavedCurrency() {
        return savedCurrency;
    }

    @Override
    public boolean getSavedNewsletterEnabled() {
        return savedNewsletterEnabled;
    }
}
