package ma.swiftrent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.swiftrent.pattern.prototype.Prototype;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Encja reprezentująca samochód dostępny do wypożyczenia.
 */
@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
/*
Tydzień 9, Niedługie metody 3
Żadna z metod w tej klasie nie przekracza 20 linijek,
ani żadna z metod w klasie wewnętrznej CarBuilder
 */
public class Car implements Prototype<Car> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private BigDecimal pricePerDay;

    @Column(name = "production_year")
    private Integer productionYear;

    private String color;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rental> rentals;

    /**
     * Status dostępności samochodu.
     */
    public enum CarStatus {
        AVAILABLE,
        UNAVAILABLE
    }

    // Tydzień 2, Wzorzec Builder 1
    // Samochód ma kilka pól opcjonalnych i wiele miejsc tworzenia w aplikacji.
    // Ręczny builder zastępuje builder Lomboka i pozwala budować obiekt krok po kroku.
    public static CarBuilder builder() {
        return new CarBuilder();
    }

    public static final class CarBuilder {
        private Long id;
        private String brand;
        private String model;
        private BigDecimal pricePerDay;
        private Integer productionYear;
        private String color;
        private String imageUrl;
        private CarStatus status = CarStatus.AVAILABLE;
        private List<Rental> rentals = new ArrayList<>();

        private CarBuilder() {
        }

        public CarBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CarBuilder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public CarBuilder model(String model) {
            this.model = model;
            return this;
        }

        public CarBuilder pricePerDay(BigDecimal pricePerDay) {
            this.pricePerDay = pricePerDay;
            return this;
        }

        public CarBuilder productionYear(Integer productionYear) {
            this.productionYear = productionYear;
            return this;
        }

        public CarBuilder color(String color) {
            this.color = color;
            return this;
        }

        public CarBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public CarBuilder status(CarStatus status) {
            this.status = status;
            return this;
        }

        public CarBuilder available() {
            this.status = CarStatus.AVAILABLE;
            return this;
        }

        public CarBuilder unavailable() {
            this.status = CarStatus.UNAVAILABLE;
            return this;
        }

        public CarBuilder rentals(List<Rental> rentals) {
            this.rentals = rentals;
            return this;
        }

        public Car build() {
            Car car = new Car();
            car.setId(id);
            car.setBrand(brand);
            car.setModel(model);
            car.setPricePerDay(pricePerDay);
            car.setProductionYear(productionYear);
            car.setColor(color);
            car.setImageUrl(imageUrl);
            car.setStatus(status);
            car.setRentals(rentals);
            return car;
        }
    }
    // Koniec, Tydzień 2, Wzorzec Builder 1

    /*
        Tydzień 2, Wzorzec Prototype 1
        Tworzy nowy obiekt klasy Car poprzez
        kopiowanie istniejacej już instancji
        za pomocą konstruktora kopiującego
    */
    public Car(Car source) {
        this.brand = source.brand;
        this.model = source.model;
        this.pricePerDay = source.pricePerDay;
        this.productionYear = source.productionYear;
        this.color = source.color;
        this.imageUrl = source.imageUrl;
        this.status = source.status;
        this.rentals = new ArrayList<>();
    }

    @Override
    public Car clone() {
        return new Car(this);
    }
    //Koniec, Tydzień 2, Wzorzec Prototype 1
}

//Koniec, Tydzień 9, Niedługie metody 3