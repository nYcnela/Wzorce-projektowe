package ma.swiftrent.pattern.strategy.pricing;

import ma.swiftrent.entity.Rental;
import ma.swiftrent.entity.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Tydzień 6, Wzorzec Strategy 1
// Końcowa cena wypożyczenia może być liczona różnymi algorytmami zależnie od czasu najmu
// i historii użytkownika. Kontekst wybiera odpowiednią strategię cenową i deleguje do niej
// obliczenie finalnej kwoty bez rozbudowywania logiki RentalService.
public final class RentalPricingStrategyContext {

    private final RentalPricingStrategy standardStrategy = new StandardPricingStrategy();
    private final RentalPricingStrategy longRentalStrategy = new LongRentalDiscountStrategy();
    private final RentalPricingStrategy loyalCustomerStrategy = new LoyalCustomerDiscountStrategy();

    public RentalPricingStrategy resolve(User user, LocalDate startDate, LocalDate endDate) {
        boolean hasCompletedRentals = user.getRentals() != null && user.getRentals().stream()
                .anyMatch(rental -> rental.getStatus() == Rental.RentalStatus.COMPLETED);

        if (hasCompletedRentals) {
            return loyalCustomerStrategy;
        }

        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (rentalDays > 7) {
            return longRentalStrategy;
        }

        return standardStrategy;
    }
}
// Koniec, Tydzień 6, Wzorzec Strategy 1
