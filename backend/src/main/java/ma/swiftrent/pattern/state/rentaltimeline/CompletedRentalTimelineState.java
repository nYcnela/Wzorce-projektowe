package ma.swiftrent.pattern.state.rentaltimeline;

public final class CompletedRentalTimelineState implements RentalTimelineState {

    @Override
    public String getLabel() {
        return "COMPLETED";
    }

    @Override
    public boolean canBeCancelledToday() {
        return false;
    }

    @Override
    public boolean isOngoing() {
        return false;
    }
}
