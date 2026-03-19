package ma.swiftrent.pattern.strategy.access;

import ma.swiftrent.entity.Rental;

// Tydzień 6, Wzorzec Strategy 2
// Operacje zwrotu i anulowania wymagają różnej polityki dostępu zależnie od relacji użytkownika
// do wypożyczenia. Kontekst wybiera strategię dla administratora, właściciela rezerwacji
// albo użytkownika bez uprawnień i deleguje do niej weryfikację dostępu.
public final class RentalAccessStrategyContext {

    private final RentalAccessStrategy adminStrategy = new AdminRentalAccessStrategy();
    private final RentalAccessStrategy ownerStrategy = new OwnerRentalAccessStrategy();
    private final RentalAccessStrategy deniedStrategy = new DeniedRentalAccessStrategy();

    public RentalAccessStrategy resolve(boolean isAdmin, Rental rental, String currentUserEmail) {
        if (isAdmin) {
            return adminStrategy;
        }

        if (rental.getUser() != null && rental.getUser().getEmail().equals(currentUserEmail)) {
            return ownerStrategy;
        }

        return deniedStrategy;
    }
}
// Koniec, Tydzień 6, Wzorzec Strategy 2
