package ma.swiftrent.service;

import ma.swiftrent.composite.CarGroup;
import ma.swiftrent.composite.CarLeaf;
import ma.swiftrent.entity.Car;
import ma.swiftrent.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FleetService {

    private final CarRepository carRepository;

    public FleetService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarGroup buildFleetTree() {
        List<Car> cars = carRepository.findAll();

        CarGroup root = new CarGroup("Flota");

        CarGroup suvGroup = new CarGroup("SUV");
        CarGroup sedanGroup = new CarGroup("Sedan");

        for (Car car : cars) {
            CarLeaf leaf = new CarLeaf(car);

            if (car.getModel().toLowerCase().contains("x") ||
                    car.getBrand().equalsIgnoreCase("BMW")) {
                suvGroup.add(leaf);
            } else {
                sedanGroup.add(leaf);
            }
        }

        root.add(suvGroup);
        root.add(sedanGroup);

        return root;
    }
}
