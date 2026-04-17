package ma.swiftrent.pattern.state.rentaltimeline;

import ma.swiftrent.entity.Rental;
import ma.swiftrent.pattern.singleton.ApplicationClock;

// Tydzień 6, Wzorzec State 3
// Faza wypożyczenia widoczna dla użytkownika zależy od statusu biznesowego i aktualnej daty.
// Kontekst wybiera stan prezentacyjny, który określa etykietę osi czasu oraz informację,
// czy wypożyczenie jest w toku i czy można je jeszcze anulować.
public final class RentalTimelineStateContext {

    private final RentalTimelineState upcomingState = new UpcomingRentalState();
    private final RentalTimelineState inProgressState = new InProgressRentalState();
    private final RentalTimelineState completedState = new CompletedRentalTimelineState();
    private final RentalTimelineState cancelledState = new CancelledRentalTimelineState();

    public RentalTimelineState resolve(Rental rental, ApplicationClock clock) {
        return switch (rental.getStatus()) {
            case COMPLETED -> completedState;
            case CANCELLED -> cancelledState;
            case ACTIVE -> rental.getStartDate().isAfter(clock.today()) ? upcomingState : inProgressState;
        };
    }
}
// Koniec, Tydzień 6, Wzorzec State 3
