package ma.swiftrent.repository;

import ma.swiftrent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium dla encji User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Znajdź użytkownika po adresie email.
     * 
     * @param email Adres email użytkownika
     * @return Optional zawierający użytkownika jeśli został znaleziony
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Sprawdź czy użytkownik o danym emailu już istnieje.
     * 
     * @param email Adres email do sprawdzenia
     * @return true jeśli użytkownik istnieje, false w przeciwnym wypadku
     */
    boolean existsByEmail(String email);
}
