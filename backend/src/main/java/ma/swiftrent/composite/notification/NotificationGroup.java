package ma.swiftrent.composite.notification;

import java.util.ArrayList;
import java.util.List;

/*
    Tydzień 7, Zasad otwarte-zamknięte 3
    NotificationGroup jest otwarte na rozbudowę w przyszłości -
    wystarczy stworzyć nowe klasy, które implementują NotificationComponent.
    Jest zamknięta na modyfikacje - nie trzeba wprowadzać żadnych zmian w
    NotificationGroup, aby ta nowa klasa działała.
 */
public class NotificationGroup implements NotificationComponent {

    private final String name;
    private final List<NotificationComponent> children = new ArrayList<>();

    public NotificationGroup(String name) {
        this.name = name;
    }

    public void add(NotificationComponent component) {
        children.add(component);
    }

    public void remove(NotificationComponent component) {
        children.remove(component);
    }

    @Override
    public void send(String message) {
        System.out.println("🔔 Grupa: " + name);

        for (NotificationComponent child : children) {
            child.send(message);
        }
    }
}
//Koniec Tydzień 7, Zasada otwarte-zamknięte 3
