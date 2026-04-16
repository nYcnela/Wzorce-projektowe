package ma.swiftrent.pattern.memento.rental;

// Wzorzec Memento – Użycie 1: zapis stanu wypożyczenia.
// Interfejs pamiątki przechowuje migawkę stanu wypożyczenia.
public interface RentalStateMemento {
    String getSavedStatus();
    String getSavedCarPlate();
    String getSavedUserEmail();
}
