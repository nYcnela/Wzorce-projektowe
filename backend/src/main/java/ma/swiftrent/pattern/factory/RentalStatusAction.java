package ma.swiftrent.pattern.factory;

import ma.swiftrent.entity.Rental;

/*
    Tydzień 3, Wzorzec Factory Method 3
    Interfejs produktu – reprezentuje operację zmieniającą stan wypożyczenia.
    Konkrety: ReturnRentalAction i CancelRentalAction (jako klasy wewnętrzne
    odpowiednich fabryk).
*/
public interface RentalStatusAction {
    void execute(Rental rental);
}
