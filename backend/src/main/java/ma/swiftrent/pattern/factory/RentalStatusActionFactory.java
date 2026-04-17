package ma.swiftrent.pattern.factory;

import ma.swiftrent.entity.Rental;

/*
    Tydzień 3, Wzorzec Factory Method 3
    Abstrakcyjna fabryka akcji zmiany statusu wypożyczenia.
    Metoda fabryczna createAction() jest abstrakcyjna – podklasa decyduje
    jaki konkretny obiekt akcji stworzyć (zwrot / anulowanie).
    Metoda process() jest metodą szablonową, która wywołuje akcję
    bez wiedzy o jej konkretnym typie.
*/
public abstract class RentalStatusActionFactory {

    public abstract RentalStatusAction createAction();

    public void process(Rental rental) {
        RentalStatusAction action = createAction();
        action.execute(rental);
    }
}
