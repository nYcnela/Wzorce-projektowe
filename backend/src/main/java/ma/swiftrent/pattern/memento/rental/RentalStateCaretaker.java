package ma.swiftrent.pattern.memento.rental;

import java.util.ArrayDeque;
import java.util.Deque;

// Wzorzec Memento – Użycie 1: zapis stanu wypożyczenia.
// Caretaker przechowuje historię pamiątek i umożliwia cofnięcie zmian.
public final class RentalStateCaretaker {

    private final Deque<RentalStateMemento> history = new ArrayDeque<>();

    public void push(RentalStateMemento memento) {
        history.push(memento);
    }

    public RentalStateMemento pop() {
        return history.pop();
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
