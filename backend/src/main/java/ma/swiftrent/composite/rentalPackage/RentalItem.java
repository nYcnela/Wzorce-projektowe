package ma.swiftrent.composite.rentalPackage;

/*
    Tydzień 3, Wzorzec Composite 3
    Zarówno pojedyńcza usługa jak i cały pakiet usług wypożyczeniowych
    implementują ten sam interfejs, czyli system może je traktować tak samo.
    Pakiet może zawierać i pojedyńcze usługi jak i pakiety
 */
public interface RentalItem {
    String getName();
    double getPrice();

}
//Koniec Tydzień 3, Wzorzec Composite 3