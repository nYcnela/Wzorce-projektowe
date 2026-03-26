package ma.swiftrent.pattern.bridge.report;

import ma.swiftrent.dto.CarResponse;

import java.util.List;

/*
    Tydzień 7, Zasada pojedyńczej odpowiedzialności 1
    Ta klasa odpoiwada jedynie za generowanie raportu;
    formatowaniem zajmuje się obiekt klasy Formatter,
    a budową danych raportu zajmuje się obiekt klasy DataBuilder.
 */
public class CarReport extends Report {

    private final List<CarResponse> cars;
    private final CarReportDataBuilder builder;

    public CarReport(List<CarResponse> cars,
                     ReportFormatter formatter,
                     CarReportDataBuilder builder) {
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
//Koniec Tydzień 7, Zasada pojecyńczej odpowiedzialności
