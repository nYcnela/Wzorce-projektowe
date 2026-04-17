package ma.swiftrent.pattern.strategy.userdeletion;

import ma.swiftrent.entity.User;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.repository.UserRepository;

public interface UserDeletionStrategy {

    void delete(User user, UserRepository userRepository, RentalRepository rentalRepository);
}
