package ma.swiftrent.pattern.iterator.pending;

// Wzorzec Iterator – Użycie 3: przeglądanie kolejki oczekujących zatwierdzeń.
// Interfejs iteratora pozwala przetwarzać kolejkę FIFO wniosków o wypożyczenie.
public interface PendingApprovalIterator {
    boolean hasNext();
    String next();
}
