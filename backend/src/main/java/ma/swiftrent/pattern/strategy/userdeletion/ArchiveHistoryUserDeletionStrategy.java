package ma.swiftrent.pattern.strategy.userdeletion;

import ma.swiftrent.entity.User;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.repository.UserRepository;

public final class ArchiveHistoryUserDeletionStrategy implements UserDeletionStrategy {

    @Override
    public void delete(User user, UserRepository userRepository, RentalRepository rentalRepository) {
        user.getRentals().forEach(rental -> {
            rental.setUser(null);
            rentalRepository.save(rental);
        });

        userRepository.delete(user);
    }
}
