package ma.swiftrent.repository;

import ma.swiftrent.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repozytorium dla encji Rental.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    
    /**
     * Znajdź wszystkie wypożyczenia dla danego użytkownika.
     * 
     * @param userId ID użytkownika
     * @return Lista wypożyczeń użytkownika
     */
    List<Rental> findByUserId(Long userId);
    
    /**
     * Sprawdź czy samochód jest dostępny w danym okresie.
     * Wyszukuje kolizje w rezerwacjach - sprawdza czy istnieją aktywne wypożyczenia
     * które nakładają się na żądany okres.
     * 
     * @param carId ID samochodu
     * @param startDate Data rozpoczęcia wypożyczenia
     * @param endDate Data zakończenia wypożyczenia
     * @return true jeśli istnieją nakładające się rezerwacje, false w przeciwnym wypadku
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Rental r WHERE r.car.id = :carId " +
           "AND r.status NOT IN ('CANCELLED', 'COMPLETED') " +
           "AND ((r.startDate <= :endDate AND r.endDate >= :startDate))")
    boolean existsOverlappingRental(
        @Param("carId") Long carId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT MAX(r.endDate) FROM Rental r WHERE r.car.id = :carId AND r.status = 'ACTIVE'")
    LocalDate findLatestEndDate(@Param("carId") Long carId);
}
