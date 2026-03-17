package ma.swiftrent.service.price;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BasicRentalPrice implements RentalPrice {

    private final BigDecimal pricePerDay;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public BasicRentalPrice(BigDecimal pricePerDay, LocalDate startDate, LocalDate endDate) {
        this.pricePerDay = pricePerDay;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public BigDecimal calculateTotalCost() {
        // Liczenie dni włącznie: z 8/01 na 9/01 = 2 dni (8/01 + 9/01)
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return pricePerDay.multiply(BigDecimal.valueOf(days));
    }
}
