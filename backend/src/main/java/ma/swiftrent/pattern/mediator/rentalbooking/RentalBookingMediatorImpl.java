package ma.swiftrent.pattern.mediator.rentalbooking;

// Wzorzec Mediator – Użycie 1: koordynacja procesu rezerwacji.
// Konkretny mediator koordynujący weryfikację dostępności auta i potwierdzenie rezerwacji.
public final class RentalBookingMediatorImpl implements RentalBookingMediator {

    @Override
    public void notify(String sender, String event) {
        if ("CarAvailabilityChecker".equals(sender) && "AVAILABLE".equals(event)) {
            System.out.println("[RentalBookingMediator] Auto dostępne – inicjuję potwierdzenie rezerwacji.");
        } else if ("PaymentService".equals(sender) && "PAID".equals(event)) {
            System.out.println("[RentalBookingMediator] Płatność potwierdzona – finalizuję rezerwację.");
        } else {
            System.out.println("[RentalBookingMediator] Zdarzenie od " + sender + ": " + event);
        }
    }
}
