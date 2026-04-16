package ma.swiftrent.pattern.memento.car;

// Wzorzec Memento – Użycie 2: zapis konfiguracji pojazdu.
// Konkretna pamiątka przechowująca migawkę modelu, statusu i stawki dziennej.
public final class CarConfigMementoImpl implements CarConfigMemento {

    private final String savedModel;
    private final String savedStatus;
    private final double savedDailyRate;

    public CarConfigMementoImpl(String savedModel, String savedStatus, double savedDailyRate) {
        this.savedModel = savedModel;
        this.savedStatus = savedStatus;
        this.savedDailyRate = savedDailyRate;
    }

    @Override
    public String getSavedModel() {
        return savedModel;
    }

    @Override
    public String getSavedStatus() {
        return savedStatus;
    }

    @Override
    public double getSavedDailyRate() {
        return savedDailyRate;
    }
}
