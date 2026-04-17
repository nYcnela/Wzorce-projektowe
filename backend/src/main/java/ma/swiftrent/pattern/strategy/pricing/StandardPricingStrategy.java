package ma.swiftrent.pattern.strategy.pricing;

import java.math.BigDecimal;

public final class StandardPricingStrategy implements RentalPricingStrategy {

    @Override
    public BigDecimal calculate(BigDecimal baseCost) {
        return baseCost;
    }
}
