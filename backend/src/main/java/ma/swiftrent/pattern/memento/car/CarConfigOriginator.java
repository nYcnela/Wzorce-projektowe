package ma.swiftrent.pattern.memento.car;

// Wzorzec Memento – Użycie 2: zapis konfiguracji pojazdu.
// Originator tworzy i przywraca pamiątkę konfiguracji pojazdu.
public final class CarConfigOriginator {

    private String model;
    private String status;
    private double dailyRate;

    public CarConfigOriginator(String model, String status, double dailyRate) {
        this.model = model;
        this.status = status;
        this.dailyRate = dailyRate;
    }

    public CarConfigMemento save() {
        return new CarConfigMementoImpl(model, status, dailyRate);
    }

    public void restore(CarConfigMemento memento) {
        this.model = memento.getSavedModel();
        this.status = memento.getSavedStatus();
        this.dailyRate = memento.getSavedDailyRate();
    }

    public String getModel() { return model; }
    public String getStatus() { return status; }
    public double getDailyRate() { return dailyRate; }

    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }
    public void setStatus(String status) { this.status = status; }
}
