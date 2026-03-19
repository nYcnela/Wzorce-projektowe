package ma.swiftrent.pattern.state.car;

import ma.swiftrent.entity.Car;

// Tydzień 6, Wzorzec State 2
// Samochód zachowuje się inaczej w zależności od tego, czy jest AVAILABLE czy UNAVAILABLE.
// Kontekst wybiera odpowiedni stan dostępności i deleguje do niego operacje zmiany statusu
// oraz obliczanie informacji o najbliższym możliwym terminie dostępności.
public final class CarAvailabilityStateContext {

    private final CarAvailabilityState availableState = new AvailableCarState();
    private final CarAvailabilityState unavailableState = new UnavailableCarState();

    public CarAvailabilityState resolve(Car car) {
        return switch (car.getStatus()) {
            case AVAILABLE -> availableState;
            case UNAVAILABLE -> unavailableState;
        };
    }
}
// Koniec, Tydzień 6, Wzorzec State 2
