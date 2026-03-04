package ma.swiftrent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO dla żądania utworzenia nowego samochodu.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {

    @NotBlank(message = "Marka nie może być pusta")
    private String brand;

    @NotBlank(message = "Model nie może być pusty")
    private String model;

    @NotNull(message = "Cena za dzień nie może być pusta")
    @Positive(message = "Cena za dzień musi być dodatnia")
    private BigDecimal pricePerDay;

    private Integer productionYear;
    private String color;
    private String imageUrl;
}
