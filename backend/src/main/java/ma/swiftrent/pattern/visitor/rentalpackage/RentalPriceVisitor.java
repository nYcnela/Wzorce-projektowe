package ma.swiftrent.pattern.visitor.rentalpackage;

import ma.swiftrent.composite.rentalPackage.RentalPackage;
import ma.swiftrent.composite.rentalPackage.RentalServiceItem;

// Tydzień 6, Wzorzec Visitor 1
// Struktura pakietu wypożyczenia pozostaje bez zmian, a Visitor dodaje do niej nową operację.
// Ten konkretny visitor przechodzi po elementach Composite i oblicza końcową cenę pakietu.
public class RentalPriceVisitor implements RentalItemVisitor {

    private double totalPrice;

    @Override
    public void visit(RentalPackage rentalPackage) {
        // Pakiet sam nie zwiększa ceny – agreguje ceny swoich dzieci.
    }

    @Override
    public void visit(RentalServiceItem rentalServiceItem) {
        totalPrice += rentalServiceItem.getPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
// Koniec, Tydzień 6, Wzorzec Visitor 1
