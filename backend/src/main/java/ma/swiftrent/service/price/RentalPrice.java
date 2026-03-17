package ma.swiftrent.service.price;

import java.math.BigDecimal;

public interface RentalPrice {
    BigDecimal calculateTotalCost();
}
