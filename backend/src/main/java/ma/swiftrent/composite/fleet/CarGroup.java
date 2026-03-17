package ma.swiftrent.composite.fleet;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CarGroup implements CarComponent {

    private final String name;

    @Getter
    private final List<CarComponent> children = new ArrayList<>();

    public CarGroup(String name) {
        this.name = name;
    }

    public void add(CarComponent component) {
        children.add(component);
    }

    public void remove(CarComponent component) {
        children.remove(component);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int countAvailableCars() {
        return children.stream()
                .mapToInt(CarComponent::countAvailableCars)
                .sum();
    }

}
