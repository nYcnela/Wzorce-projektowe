package ma.swiftrent.pattern.state.car;

import ma.swiftrent.entity.Car;

import java.time.LocalDate;

public interface CarAvailabilityState {

    boolean isAvailable();

    void markAvailable(Car car);

    void markUnavailable(Car car);

    LocalDate resolveAvailableFrom(LocalDate latestEndDate);
}
