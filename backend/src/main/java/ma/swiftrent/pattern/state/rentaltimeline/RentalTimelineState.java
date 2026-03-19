package ma.swiftrent.pattern.state.rentaltimeline;

public interface RentalTimelineState {

    String getLabel();

    boolean canBeCancelledToday();

    boolean isOngoing();
}
