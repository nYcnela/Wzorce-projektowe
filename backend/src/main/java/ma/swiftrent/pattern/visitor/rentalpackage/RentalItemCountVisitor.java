package ma.swiftrent.pattern.visitor.rentalpackage;

import ma.swiftrent.composite.rentalPackage.RentalPackage;
import ma.swiftrent.composite.rentalPackage.RentalServiceItem;

// Tydzień 6, Wzorzec Visitor 3
// Ta sama struktura Composite może być odwiedzana przez innego visitora,
// który nie liczy ceny ani nie buduje opisu, tylko zlicza elementy pakietu.
public class RentalItemCountVisitor implements RentalItemVisitor {

    private int packageCount;
    private int serviceItemCount;

    @Override
    public void visit(RentalPackage rentalPackage) {
        packageCount++;
    }

    @Override
    public void visit(RentalServiceItem rentalServiceItem) {
        serviceItemCount++;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public int getServiceItemCount() {
        return serviceItemCount;
    }
}
// Koniec, Tydzień 6, Wzorzec Visitor 3
