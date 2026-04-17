package ma.swiftrent.pattern.mediator.carreturn;

// Wzorzec Mediator – Użycie 2: koordynacja procesu zwrotu pojazdu.
// Konkretny mediator koordynujący inspekcję auta i aktualizację jego statusu po zwrocie.
public final class CarReturnMediatorImpl implements CarReturnMediator {

    @Override
    public void notify(String sender, String event) {
        if ("InspectionService".equals(sender) && "INSPECTION_PASSED".equals(event)) {
            System.out.println("[CarReturnMediator] Inspekcja zaliczona – oznaczam auto jako dostępne.");
        } else if ("InspectionService".equals(sender) && "DAMAGE_FOUND".equals(event)) {
            System.out.println("[CarReturnMediator] Wykryto uszkodzenie – przekazuję do serwisu.");
        } else {
            System.out.println("[CarReturnMediator] Zdarzenie od " + sender + ": " + event);
        }
    }
}
