package ma.swiftrent.pattern.bridge.report;

import ma.swiftrent.dto.RentalResponse;

import java.util.List;

public class RentalReport extends Report {

    private final List<RentalResponse> rentals;

    public RentalReport(List<RentalResponse> rentals, ReportFormatter formatter) {
        super(formatter);
        this.rentals = rentals;
    }

    @Override
    public String generate() {
        String data = String.join(" | ", rentals.toString());
        return formatter.format(data);
    }
}
