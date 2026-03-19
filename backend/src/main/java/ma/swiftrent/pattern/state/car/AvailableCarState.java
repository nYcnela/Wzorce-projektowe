package ma.swiftrent.pattern.state.car;

import ma.swiftrent.entity.Car;

import java.time.LocalDate;

public final class AvailableCarState implements CarAvailabilityState {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void markAvailable(Car car) {
        car.setStatus(Car.CarStatus.AVAILABLE);
    }

    @Override
    public void markUnavailable(Car car) {
        car.setStatus(Car.CarStatus.UNAVAILABLE);
    }

    @Override
    public LocalDate resolveAvailableFrom(LocalDate latestEndDate) {
        return null;
    }
}
