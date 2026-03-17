package ma.swiftrent.composite.fleet;

import ma.swiftrent.entity.Car;

public class CarLeaf implements CarComponent {

    private final Car car;

    public CarLeaf(Car car) {
        this.car = car;
    }

    @Override
    public String getName() {
        return car.getBrand() + " " + car.getModel();
    }

    @Override
    public int countAvailableCars() {
        return car.getStatus() == Car.CarStatus.AVAILABLE ? 1 : 0;
    }

    public Car getCar() {
        return car;
    }
}
