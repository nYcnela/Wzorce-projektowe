package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.dto.UserResponse;
import ma.swiftrent.entity.Car;
import ma.swiftrent.entity.User;
import ma.swiftrent.pattern.factory.CarResponseFactory;
import ma.swiftrent.pattern.factory.UserResponseFactory;
import ma.swiftrent.pattern.observer.favorite.FavoriteChangedEvent;
import ma.swiftrent.pattern.observer.favorite.FavoriteChangedSubject;
import ma.swiftrent.pattern.strategy.userdeletion.UserDeletionStrategyContext;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final ma.swiftrent.repository.RentalRepository rentalRepository;
    private final FavoriteChangedSubject favoriteChangedSubject;
    // Tydzień 6, Wzorzec Strategy 3 – użycie UserDeletionStrategyContext (Context)
    private final UserDeletionStrategyContext userDeletionStrategyContext = new UserDeletionStrategyContext();

    // Tydzień 3, Wzorzec Factory Method 3 – użycie UserResponseFactory i CarResponseFactory (ConcreteCreator)
    private final UserResponseFactory userResponseFactory = new UserResponseFactory();
    private final CarResponseFactory carResponseFactory = new CarResponseFactory();

    public List<UserResponse> getAllUsers() {
        return userResponseFactory.createAll(userRepository.findAll());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        // Tydzień 6, Wzorzec Strategy 3 – wybór sposobu usunięcia użytkownika
        userDeletionStrategyContext.resolve(user).delete(user, userRepository, rentalRepository);
    }

    @Transactional
    public void toggleFavorite(String userEmail, Long carId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Samochód nie znaleziony"));

        boolean added;
        if (user.getFavorites().contains(car)) {
            user.getFavorites().remove(car);
            added = false;
        } else {
            user.getFavorites().add(car);
            added = true;
        }
        userRepository.save(user);
        favoriteChangedSubject.notifyObservers(new FavoriteChangedEvent(userEmail, carId, added));
    }

    @Transactional(readOnly = true)
    public List<CarResponse> getUserFavorites(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        return carResponseFactory.createAll(user.getFavorites());
    }
}
