package ma.swiftrent.pattern.visitor.rentalpackage;

import ma.swiftrent.composite.rentalPackage.RentalPackage;
import ma.swiftrent.composite.rentalPackage.RentalServiceItem;

import java.util.StringJoiner;

// Tydzień 6, Wzorzec Visitor 2
// Visitor pozwala dodać operację budowania opisu bez dokładania nowych metod
// do klas RentalPackage i RentalServiceItem. Ten visitor tworzy tekstowy opis pakietu.
public class RentalDescriptionVisitor implements RentalItemVisitor {

    private final StringJoiner description = new StringJoiner(" | ");

    @Override
    public void visit(RentalPackage rentalPackage) {
        description.add("Pakiet: " + rentalPackage.getName());
    }

    @Override
    public void visit(RentalServiceItem rentalServiceItem) {
        description.add("Usługa: " + rentalServiceItem.getName() + " (" + rentalServiceItem.getPrice() + " zł)");
    }

    public String getDescription() {
        return description.toString();
    }
}
// Koniec, Tydzień 6, Wzorzec Visitor 2
