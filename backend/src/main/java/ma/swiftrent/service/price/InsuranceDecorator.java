package ma.swiftrent.service.price;

import java.math.BigDecimal;

public class InsuranceDecorator extends RentalPriceDecorator {

    public InsuranceDecorator(RentalPrice decoratedPrice) {
        super(decoratedPrice);
    }

    @Override
    public BigDecimal calculateTotalCost() {
        return basicPrice.calculateTotalCost()
                .add(BigDecimal.valueOf(50));
    }
}
