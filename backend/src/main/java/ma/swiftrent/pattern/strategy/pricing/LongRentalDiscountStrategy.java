package ma.swiftrent.pattern.strategy.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class LongRentalDiscountStrategy implements RentalPricingStrategy {

    private static final BigDecimal DISCOUNT_MULTIPLIER = new BigDecimal("0.90");

    @Override
    public BigDecimal calculate(BigDecimal baseCost) {
        return baseCost.multiply(DISCOUNT_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
    }
}
