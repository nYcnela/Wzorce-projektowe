package ma.swiftrent.pattern.iterator.pending;

import java.util.LinkedList;
import java.util.Queue;

// Wzorzec Iterator – Użycie 3: przeglądanie kolejki oczekujących zatwierdzeń.
// Konkretny iterator oparty na kolejce FIFO wniosków oczekujących na zatwierdzenie.
public final class PendingApprovalIteratorImpl implements PendingApprovalIterator {

    private final Queue<String> queue;

    public PendingApprovalIteratorImpl(Queue<String> pendingRequests) {
        this.queue = new LinkedList<>(pendingRequests);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public String next() {
        return queue.poll();
    }
}
