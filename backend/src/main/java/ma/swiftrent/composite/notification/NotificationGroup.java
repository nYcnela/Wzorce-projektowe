package ma.swiftrent.composite.notification;

import java.util.ArrayList;
import java.util.List;

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
