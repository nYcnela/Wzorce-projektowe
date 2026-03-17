package ma.swiftrent.service.price;

/*
    Tydzień 3, Wzorzec Dekorator 1
    Dokorator usługi obliczania ceny wypożyczenia
    Można "udekorować" tą funkcjonalnosć o wyliczenie
    ceny za dodanie ubezpieczenia i gps
 */
public abstract class RentalPriceDecorator implements RentalPrice {

    protected final RentalPrice basicPrice;

    public RentalPriceDecorator(RentalPrice basicPrice) {
        this.basicPrice = basicPrice;
    }

}
// Koniec Tydzień 3, Wzorzec Dekorator 1
