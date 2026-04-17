package ma.swiftrent.pattern.memento.user;

// Wzorzec Memento – Użycie 3: zapis preferencji użytkownika.
// Originator tworzy i przywraca pamiątkę preferencji użytkownika.
public final class UserPreferencesOriginator {

    private String language;
    private String currency;
    private boolean newsletterEnabled;

    public UserPreferencesOriginator(String language, String currency, boolean newsletterEnabled) {
        this.language = language;
        this.currency = currency;
        this.newsletterEnabled = newsletterEnabled;
    }

    public UserPreferencesMemento save() {
        return new UserPreferencesMementoImpl(language, currency, newsletterEnabled);
    }

    public void restore(UserPreferencesMemento memento) {
        this.language = memento.getSavedLanguage();
        this.currency = memento.getSavedCurrency();
        this.newsletterEnabled = memento.getSavedNewsletterEnabled();
    }

    public String getLanguage() { return language; }
    public String getCurrency() { return currency; }
    public boolean isNewsletterEnabled() { return newsletterEnabled; }

    public void setLanguage(String language) { this.language = language; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setNewsletterEnabled(boolean newsletterEnabled) { this.newsletterEnabled = newsletterEnabled; }
}
