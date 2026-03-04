package ma.swiftrent.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.swiftrent.entity.Car;
import ma.swiftrent.entity.User;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Ładuje przykładowe dane do bazy podczas startu aplikacji.
 * Tworzy użytkownika Admin, użytkownika User oraz 5 przykładowych samochodów.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        loadUsers();
        loadCars();
    }

    /**
     * Ładuje przykładowych użytkowników.
     */
    private void loadUsers() {
        if (userRepository.count() > 0) {
            log.info("Użytkownicy już istnieją w bazie.");
            return;
        }

        User admin = User.builder()
                .email("admin@swiftrent.pl")
                .password(passwordEncoder.encode("admin"))
                .role(User.Role.ADMIN)
                .build();

        User user = User.builder()
                .email("user@swiftrent.pl")
                .password(passwordEncoder.encode("user"))
                .role(User.Role.USER)
                .build();

        userRepository.saveAll(Arrays.asList(admin, user));
        
        log.info("Utworzono użytkowników testowych:");
        log.info("   - Admin: admin / admin");
        log.info("   - User: user / user");
    }

    /**
     * Ładuje przykładowe samochody.
     */
    private void loadCars() {
        if (carRepository.count() > 0) {
            log.info("Samochody już istnieją w bazie.");
            return;
        }

        List<Car> cars = Arrays.asList(
                Car.builder()
                        .brand("Toyota")
                        .model("Corolla")
                        .pricePerDay(new BigDecimal("150.00"))
                        .productionYear(2020)
                        .color("biały")
                        .imageUrl("https://images.unsplash.com/photo-1623869675781-80aa31012a5a?w=400")
                        .status(Car.CarStatus.AVAILABLE)
                        .build(),

                Car.builder()
                        .brand("BMW")
                        .model("X5")
                        .pricePerDay(new BigDecimal("350.00"))
                        .productionYear(2025)
                        .color("ciemno zielony")
                        .imageUrl("http://localhost:8080/uploads/bmwx5.png")
                        .status(Car.CarStatus.AVAILABLE)
                        .build(),

                Car.builder()
                        .brand("Mercedes")
                        .model("C-Class")
                        .pricePerDay(new BigDecimal("300.00"))
                        .productionYear(2024)
                        .color("niebieski")
                        .imageUrl("http://localhost:8080/uploads/cklasa.png")
                        .status(Car.CarStatus.AVAILABLE)
                        .build(),

                Car.builder()
                        .brand("Audi")
                        .model("A4")
                        .pricePerDay(new BigDecimal("280.00"))
                        .productionYear(2022)
                        .color("srebrny")
                        .imageUrl("http://localhost:8080/uploads/audia4.png")
                        .status(Car.CarStatus.AVAILABLE)
                        .build(),

                Car.builder()
                        .brand("Volkswagen")
                        .model("Golf")
                        .pricePerDay(new BigDecimal("120.00"))
                        .productionYear(1998)
                        .color("srebrny")
                        .imageUrl("http://localhost:8080/uploads/golf.png")
                        .status(Car.CarStatus.AVAILABLE)
                        .build()
        );

        carRepository.saveAll(cars);
        
        log.info("Utworzono 5 przykładowych samochodów:");
        cars.forEach(car -> log.info("   - {} {} - {}zł/dzień", 
                car.getBrand(), car.getModel(), car.getPricePerDay()));
    }
}
