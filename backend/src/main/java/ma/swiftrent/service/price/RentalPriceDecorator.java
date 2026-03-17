package ma.swiftrent.service.price;

public abstract class RentalPriceDecorator implements RentalPrice {

    protected final RentalPrice basicPrice;

    public RentalPriceDecorator(RentalPrice basicPrice) {
        this.basicPrice = basicPrice;
    }

}
