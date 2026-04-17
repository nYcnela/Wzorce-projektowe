package ma.swiftrent.pattern.bridge.report;

/*
    Tydzień 3, Wzorzec Bridge 3
    oddzielenie formatu raportu od treści reportu.
    Uniknięcie potrzeby tworzenia kombinacji typu:
    CarTextReportFormatter, RentalJsonReportFormatter
 */

public abstract class Report {

    protected final ReportFormatter formatter;

    protected Report(ReportFormatter formatter) {
        this.formatter = formatter;
    }

    public abstract String generate();
}
//Koniec Tydzień 3, Wzorzec 3
