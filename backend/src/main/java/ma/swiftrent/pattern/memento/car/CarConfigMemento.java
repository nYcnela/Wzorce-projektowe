package ma.swiftrent.pattern.memento.car;

// Wzorzec Memento – Użycie 2: zapis konfiguracji pojazdu.
// Interfejs pamiątki przechowuje migawkę konfiguracji auta (model, cena, status).
public interface CarConfigMemento {
    String getSavedModel();
    String getSavedStatus();
    double getSavedDailyRate();
}
