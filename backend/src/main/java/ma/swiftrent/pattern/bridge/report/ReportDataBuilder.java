package ma.swiftrent.pattern.bridge.report;

import java.util.List;

public interface ReportDataBuilder<T> {
    String build(List<T> data);
}
