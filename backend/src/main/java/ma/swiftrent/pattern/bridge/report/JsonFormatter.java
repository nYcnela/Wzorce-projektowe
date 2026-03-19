package ma.swiftrent.pattern.bridge.report;

public class JsonFormatter implements ReportFormatter {

    @Override
    public String format(String data) {
        return "{ \"report\": \"" + data + "\" }";
    }
}
