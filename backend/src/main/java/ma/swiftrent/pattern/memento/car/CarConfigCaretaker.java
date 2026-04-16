package ma.swiftrent.pattern.memento.car;

import java.util.ArrayDeque;
import java.util.Deque;

// Wzorzec Memento – Użycie 2: zapis konfiguracji pojazdu.
// Caretaker przechowuje historię pamiątek konfiguracji pojazdu.
public final class CarConfigCaretaker {

    private final Deque<CarConfigMemento> history = new ArrayDeque<>();

    public void push(CarConfigMemento memento) {
        history.push(memento);
    }

    public CarConfigMemento pop() {
        return history.pop();
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
