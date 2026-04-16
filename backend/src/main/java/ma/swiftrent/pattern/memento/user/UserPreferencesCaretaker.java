package ma.swiftrent.pattern.memento.user;

import java.util.ArrayDeque;
import java.util.Deque;

// Wzorzec Memento – Użycie 3: zapis preferencji użytkownika.
// Caretaker przechowuje historię pamiątek preferencji i umożliwia cofnięcie zmian.
public final class UserPreferencesCaretaker {

    private final Deque<UserPreferencesMemento> history = new ArrayDeque<>();

    public void push(UserPreferencesMemento memento) {
        history.push(memento);
    }

    public UserPreferencesMemento pop() {
        return history.pop();
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
