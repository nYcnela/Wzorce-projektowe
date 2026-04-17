package ma.swiftrent.pattern.factory;

import ma.swiftrent.entity.Rental;

import java.math.BigDecimal;

/*
    Tydzień 3, Wzorzec Factory Method 3 – ConcreteCreator: tworzy akcję anulowania rezerwacji.
    Konkretny produkt (CancelRentalAction) jest klasą wewnętrzną – ustawia status
    na CANCELLED i zeruje koszt.
*/
public class CancelRentalActionFactory extends RentalStatusActionFactory {

    @Override
    public RentalStatusAction createAction() {
        return new CancelRentalAction();
    }

    // ConcreteProduct – akcja anulowania rezerwacji
    private static class CancelRentalAction implements RentalStatusAction {

        @Override
        public void execute(Rental rental) {
            rental.setStatus(Rental.RentalStatus.CANCELLED);
            rental.setTotalCost(BigDecimal.ZERO);
        }
    }
}
