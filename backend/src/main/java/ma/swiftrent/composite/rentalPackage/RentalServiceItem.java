package ma.swiftrent.composite.rentalPackage;

import ma.swiftrent.pattern.visitor.rentalpackage.RentalItemVisitor;

public class RentalServiceItem implements RentalItem {

    private final String name;
    private final double price;

    public RentalServiceItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void accept(RentalItemVisitor visitor) {
        visitor.visit(this);
    }
}
