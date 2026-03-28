package ma.swiftrent.pattern.bridge.report;

import ma.swiftrent.dto.CarResponse;

import java.util.List;

/*
    Tydzień 7, Zasada pojedyńczej odpowiedzialności 1
    Ta klasa odpowiada jedynie za generowanie raportu;
    formatowaniem zajmuje się obiekt klasy Formatter,
    a budową danych raportu zajmuje się obiekt klasy DataBuilder.

    Tydzień 7, Zasada otwarte-zamknięte 2
    Zarówno formater jak i builder są interfejsami, więc ta klasa
    jest otwarta na nowe formatu i budowy w przyszłości (w formie nowych
    klas implementujących dany interfejs), ale sama klasa jest zamknięta na zmiany
 */
public class CarReport extends Report {

    private final List<CarResponse> cars;
    private final ReportDataBuilder<CarResponse> builder;

    public CarReport(List<CarResponse> cars,
                     ReportFormatter formatter,
                     ReportDataBuilder<CarResponse> builder) {
        super(formatter);
        this.cars = cars;
        this.builder = builder;
    }

    @Override
    public String generate() {
        String data = builder.build(cars);
        return formatter.format(data);
    }
}
//Koniec Tydzień 7, Zasada pojedyńczej odpowiedzialności 1
//Koniec Tydzień 7, Zasada otwarte-zamknięte 2
