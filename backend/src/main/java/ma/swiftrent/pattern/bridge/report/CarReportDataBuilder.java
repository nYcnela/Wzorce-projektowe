package ma.swiftrent.pattern.bridge.report;

import ma.swiftrent.dto.CarResponse;
import java.util.List;
import java.util.stream.Collectors;

public class CarReportDataBuilder {

    public String build(List<CarResponse> cars) {
        return cars.stream()
                .map(CarResponse::toString)
                .collect(Collectors.joining(", "));
    }
}
