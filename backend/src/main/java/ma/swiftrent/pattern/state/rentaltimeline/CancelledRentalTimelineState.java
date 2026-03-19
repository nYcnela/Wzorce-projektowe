package ma.swiftrent.pattern.state.rentaltimeline;

public final class CancelledRentalTimelineState implements RentalTimelineState {

    @Override
    public String getLabel() {
        return "CANCELLED";
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
