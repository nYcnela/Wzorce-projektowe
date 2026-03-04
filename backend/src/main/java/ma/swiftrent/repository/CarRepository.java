package ma.swiftrent.repository;

import ma.swiftrent.entity.Car;
import ma.swiftrent.entity.Car.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium dla encji Car.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    
    /**
     * Znajdź wszystkie samochody o określonym statusie.
     * 
     * @param status Status samochodu (AVAILABLE/UNAVAILABLE)
     * @return Lista samochodów o danym statusie
     */
    List<Car> findByStatus(CarStatus status);
}
