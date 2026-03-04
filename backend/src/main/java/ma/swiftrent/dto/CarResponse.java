package ma.swiftrent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.swiftrent.entity.Car;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dla odpowiedzi z danymi samochodu.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {

    private Long id;
    private String brand;
    private String model;
    private BigDecimal pricePerDay;
    private Integer productionYear;
    private String color;
    private String imageUrl;
    private String status;
    private boolean available;
    private LocalDate availableFrom;

    /**
     * Konwertuje encję Car na CarResponse.
     */
    public static CarResponse fromEntity(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .pricePerDay(car.getPricePerDay())
                .productionYear(car.getProductionYear())
                .color(car.getColor())
                .imageUrl(car.getImageUrl())
                .status(car.getStatus().name())
                .available(car.getStatus() == Car.CarStatus.AVAILABLE)
                .build();
    }
}
