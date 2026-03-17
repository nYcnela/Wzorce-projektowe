package ma.swiftrent.service.price;

import java.math.BigDecimal;

public class GpsDecorator extends RentalPriceDecorator {

    public GpsDecorator(RentalPrice decoratedPrice) {
        super(decoratedPrice);
    }

    @Override
    public BigDecimal calculateTotalCost() {
        return basicPrice.calculateTotalCost()
                .add(BigDecimal.valueOf(20));
    }
}
