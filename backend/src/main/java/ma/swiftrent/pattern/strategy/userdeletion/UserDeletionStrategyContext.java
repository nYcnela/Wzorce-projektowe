package ma.swiftrent.pattern.strategy.userdeletion;

import ma.swiftrent.entity.Rental;
import ma.swiftrent.entity.User;

// Tydzień 6, Wzorzec Strategy 3
// Usuwanie użytkownika zależy od stanu jego historii wypożyczeń. Kontekst wybiera strategię,
// która albo blokuje operację, albo archiwizuje historię rezerwacji, albo wykonuje proste usunięcie,
// dzięki czemu UserService nie zawiera wszystkich wariantów kasowania w jednej metodzie.
public final class UserDeletionStrategyContext {

    private final UserDeletionStrategy blockedStrategy = new BlockedUserDeletionStrategy();
    private final UserDeletionStrategy archiveHistoryStrategy = new ArchiveHistoryUserDeletionStrategy();
    private final UserDeletionStrategy simpleDeletionStrategy = new SimpleUserDeletionStrategy();

    public UserDeletionStrategy resolve(User user) {
        boolean hasActiveRentals = user.getRentals().stream()
                .anyMatch(rental -> rental.getStatus() == Rental.RentalStatus.ACTIVE);

        if (hasActiveRentals) {
            return blockedStrategy;
        }

        if (user.getRentals().isEmpty()) {
            return simpleDeletionStrategy;
        }

        return archiveHistoryStrategy;
    }
}
// Koniec, Tydzień 6, Wzorzec Strategy 3
