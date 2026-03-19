package ma.swiftrent.pattern.bridge.report;

import ma.swiftrent.dto.CarResponse;

import java.util.List;

public class CarReport extends Report {

    private final List<CarResponse> cars;

    public CarReport(List<CarResponse> cars, ReportFormatter formatter) {
        super(formatter);
        this.cars = cars;
    }

    @Override
    public String generate() {
        String data = String.join(", ", cars.toString());
        return formatter.format(data);
    }
}
