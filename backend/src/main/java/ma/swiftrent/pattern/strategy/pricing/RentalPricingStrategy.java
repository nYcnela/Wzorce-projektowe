package ma.swiftrent.pattern.strategy.pricing;

import java.math.BigDecimal;

public interface RentalPricingStrategy {

    BigDecimal calculate(BigDecimal baseCost);
}
