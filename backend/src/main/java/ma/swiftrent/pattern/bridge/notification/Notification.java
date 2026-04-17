package ma.swiftrent.pattern.bridge.notification;

/*
    Tydzień 3, Wzorzec Bridge 1
    Oddzielenie tego co robimy od tego jak to robimy
    w przypadku wysyłania powiadomień.
    Możemy dowolnie łączyć to jakiego rodzaju powiadomienie
    wysyłamy i to za pomocą jakiego kanału je wysyłamy.
 */
public abstract class Notification {

    protected final NotificationSender sender;

    protected Notification(NotificationSender sender) {
        this.sender = sender;
    }

    public abstract void send(String message);
}
//Koniec Tydzień 3, Wzorzec Bridge 1
