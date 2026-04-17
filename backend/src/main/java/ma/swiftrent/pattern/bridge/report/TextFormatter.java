package ma.swiftrent.pattern.bridge.report;

public class TextFormatter implements ReportFormatter {

    @Override
    public String format(String data) {
        return "REPORT: " + data;
    }
}
