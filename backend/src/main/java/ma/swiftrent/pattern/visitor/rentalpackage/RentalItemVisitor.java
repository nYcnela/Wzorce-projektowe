package ma.swiftrent.pattern.visitor.rentalpackage;

import ma.swiftrent.composite.rentalPackage.RentalPackage;
import ma.swiftrent.composite.rentalPackage.RentalServiceItem;

public interface RentalItemVisitor {
    void visit(RentalPackage rentalPackage);
    void visit(RentalServiceItem rentalServiceItem);
}
