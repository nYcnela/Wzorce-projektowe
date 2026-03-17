package ma.swiftrent.pattern.factory;

import ma.swiftrent.entity.Car;
import ma.swiftrent.entity.Rental;
import ma.swiftrent.pattern.singleton.ApplicationClock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
    Tydzień 3, Wzorzec Factory Method 3 – ConcreteCreator: tworzy akcję zwrotu samochodu.
    Konkretny produkt (ReturnRentalAction) jest klasą wewnętrzną – enkapsuluje logikę
    zmiany statusu i przeliczenia kosztu na faktyczny czas wypożyczenia.
*/
public class ReturnRentalActionFactory extends RentalStatusActionFactory {

    private final ApplicationClock clock;

    public ReturnRentalActionFactory(ApplicationClock clock) {
        this.clock = clock;
    }

    @Override
    public RentalStatusAction createAction() {
        return new ReturnRentalAction(clock);
    }

    // ConcreteProduct – akcja zwrotu samochodu
    private static class ReturnRentalAction implements RentalStatusAction {

        private final ApplicationClock clock;

        ReturnRentalAction(ApplicationClock clock) {
            this.clock = clock;
        }

        @Override
        public void execute(Rental rental) {
            LocalDate today = clock.today();
            rental.setEndDate(today);

            BigDecimal actualCost;
            if (today.isBefore(rental.getStartDate())) {
                // Zwrot przed datą rozpoczęcia – brak opłaty
                actualCost = BigDecimal.ZERO;
            } else {
                long days = ChronoUnit.DAYS.between(rental.getStartDate(), today) + 1;
                actualCost = rental.getCar().getPricePerDay().multiply(BigDecimal.valueOf(days));
            }

            rental.setTotalCost(actualCost);
            rental.setStatus(Rental.RentalStatus.COMPLETED);
            rental.getCar().setStatus(Car.CarStatus.AVAILABLE);
        }
    }
}
