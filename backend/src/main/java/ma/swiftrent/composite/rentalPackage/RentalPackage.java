package ma.swiftrent.composite.rentalPackage;

import lombok.Getter;
import ma.swiftrent.pattern.visitor.rentalpackage.RentalItemVisitor;

import java.util.ArrayList;
import java.util.List;

public class RentalPackage implements RentalItem {

    private final String name;
    @Getter
    private final List<RentalItem> items = new ArrayList<>();

    public RentalPackage(String name) {
        this.name = name;
    }

    public void add(RentalItem item) {
        items.add(item);
    }

    public void remove(RentalItem item) {
        items.remove(item);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return items.stream()
                .mapToDouble(RentalItem::getPrice)
                .sum();
    }

    @Override
    public void accept(RentalItemVisitor visitor) {
        visitor.visit(this);
        items.forEach(item -> item.accept(visitor));
    }
}
