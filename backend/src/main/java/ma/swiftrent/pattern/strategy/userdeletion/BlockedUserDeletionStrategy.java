package ma.swiftrent.pattern.strategy.userdeletion;

import ma.swiftrent.entity.User;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.repository.UserRepository;

public final class BlockedUserDeletionStrategy implements UserDeletionStrategy {

    @Override
    public void delete(User user, UserRepository userRepository, RentalRepository rentalRepository) {
        throw new RuntimeException("Nie można usunąć użytkownika z aktywnymi wypożyczeniami");
    }
}
