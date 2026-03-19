package ma.swiftrent.pattern.bridge.report;

public class CsvFormatter implements ReportFormatter {

    @Override
    public String format(String data) {
        return "REPORT," + data;
    }
}
