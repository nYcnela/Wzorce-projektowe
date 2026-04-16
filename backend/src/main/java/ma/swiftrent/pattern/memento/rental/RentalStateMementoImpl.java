package ma.swiftrent.pattern.memento.rental;

// Wzorzec Memento – Użycie 1: zapis stanu wypożyczenia.
// Konkretna pamiątka przechowująca migawkę statusu, tablicy rejestracyjnej i e-maila.
public final class RentalStateMementoImpl implements RentalStateMemento {

    private final String savedStatus;
    private final String savedCarPlate;
    private final String savedUserEmail;

    public RentalStateMementoImpl(String savedStatus, String savedCarPlate, String savedUserEmail) {
        this.savedStatus = savedStatus;
        this.savedCarPlate = savedCarPlate;
        this.savedUserEmail = savedUserEmail;
    }

    @Override
    public String getSavedStatus() {
        return savedStatus;
    }

    @Override
    public String getSavedCarPlate() {
        return savedCarPlate;
    }

    @Override
    public String getSavedUserEmail() {
        return savedUserEmail;
    }
}
