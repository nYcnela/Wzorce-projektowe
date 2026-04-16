package ma.swiftrent.pattern.memento.rental;

// Wzorzec Memento – Użycie 1: zapis stanu wypożyczenia.
// Originator tworzy i przywraca pamiątkę stanu wypożyczenia.
public final class RentalStateOriginator {

    private String status;
    private String carPlate;
    private String userEmail;

    public RentalStateOriginator(String status, String carPlate, String userEmail) {
        this.status = status;
        this.carPlate = carPlate;
        this.userEmail = userEmail;
    }

    public RentalStateMemento save() {
        return new RentalStateMementoImpl(status, carPlate, userEmail);
    }

    public void restore(RentalStateMemento memento) {
        this.status = memento.getSavedStatus();
        this.carPlate = memento.getSavedCarPlate();
        this.userEmail = memento.getSavedUserEmail();
    }

    public String getStatus() { return status; }
    public String getCarPlate() { return carPlate; }
    public String getUserEmail() { return userEmail; }

    public void setStatus(String status) { this.status = status; }
}
