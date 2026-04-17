package ma.swiftrent.pattern.state.rentaltimeline;

public final class InProgressRentalState implements RentalTimelineState {

    @Override
    public String getLabel() {
        return "IN_PROGRESS";
    }

    @Override
    public boolean canBeCancelledToday() {
        return false;
    }

    @Override
    public boolean isOngoing() {
        return true;
    }
}
