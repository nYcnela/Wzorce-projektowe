package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.dto.UserResponse;
import ma.swiftrent.entity.Car;
import ma.swiftrent.entity.User;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final ma.swiftrent.repository.RentalRepository rentalRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        
        // Sprawdza czy użytkownik ma aktywne wypożyczenia
        boolean hasActiveRentals = user.getRentals().stream()
                .anyMatch(rental -> rental.getStatus() == ma.swiftrent.entity.Rental.RentalStatus.ACTIVE);
        
        if (hasActiveRentals) {
            throw new RuntimeException("Nie można usunąć użytkownika z aktywnymi wypożyczeniami");
        }
        
        // Odpinanie zakończonych wypożyczeń od użytkownika zamiast je usuwać (zachowanie historii)
        user.getRentals().forEach(rental -> {
            rental.setUser(null);
            rentalRepository.save(rental);
        });
        
        userRepository.delete(user);
    }

    @Transactional
    public void toggleFavorite(String userEmail, Long carId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Samochód nie znaleziony"));

        if (user.getFavorites().contains(car)) {
            user.getFavorites().remove(car);
        } else {
            user.getFavorites().add(car);
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<CarResponse> getUserFavorites(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        return user.getFavorites().stream()
                .map(car -> {
                    CarResponse response = CarResponse.fromEntity(car);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
