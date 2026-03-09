package ma.swiftrent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Encja reprezentująca wypożyczenie samochodu przez użytkownika.
 */
@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    /**
     * Status wypożyczenia.
     */
    public enum RentalStatus {
        ACTIVE,     // Trwa lub zaplanowane wypożyczenie
        COMPLETED,  // Zwrócono samochód
        CANCELLED   // Anulowano rezerwację
    }

    // Tydzień 2, Wzorzec Builder 3
    // Wypożyczenie powstaje z wielu danych domenowych: użytkownika, auta, dat i kosztu.
    // Ręczny builder zastępuje builder Lomboka i upraszcza tworzenie spójnych rezerwacji.
    public static RentalBuilder builder() {
        return new RentalBuilder();
    }

    public static final class RentalBuilder {
        private Long id;
        private User user;
        private Car car;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal totalCost;
        private RentalStatus status = RentalStatus.ACTIVE;

        private RentalBuilder() {
        }

        public RentalBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RentalBuilder user(User user) {
            this.user = user;
            return this;
        }

        public RentalBuilder car(Car car) {
            this.car = car;
            return this;
        }

        public RentalBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public RentalBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public RentalBuilder totalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public RentalBuilder status(RentalStatus status) {
            this.status = status;
            return this;
        }

        public RentalBuilder active() {
            this.status = RentalStatus.ACTIVE;
            return this;
        }

        public RentalBuilder completed() {
            this.status = RentalStatus.COMPLETED;
            return this;
        }

        public RentalBuilder cancelled() {
            this.status = RentalStatus.CANCELLED;
            return this;
        }

        public Rental build() {
            Rental rental = new Rental();
            rental.setId(id);
            rental.setUser(user);
            rental.setCar(car);
            rental.setStartDate(startDate);
            rental.setEndDate(endDate);
            rental.setTotalCost(totalCost);
            rental.setStatus(status);
            return rental;
        }
    }
    // Koniec, Tydzień 2, Wzorzec Builder 3
}
