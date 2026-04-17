package ma.swiftrent.pattern.state.rentaltimeline;

public final class UpcomingRentalState implements RentalTimelineState {

    @Override
    public String getLabel() {
        return "UPCOMING";
    }

    @Override
    public boolean canBeCancelledToday() {
        return true;
    }

    @Override
    public boolean isOngoing() {
        return false;
    }
}
