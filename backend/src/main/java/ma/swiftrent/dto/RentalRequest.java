package ma.swiftrent.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO dla żądania utworzenia nowego wypożyczenia.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {

    @NotNull(message = "ID samochodu nie może być puste")
    private Long carId;

    @NotNull(message = "Data rozpoczęcia nie może być pusta")
    @FutureOrPresent(message = "Data rozpoczęcia nie może być w przeszłości")
    private LocalDate startDate;

    @NotNull(message = "Data zakończenia nie może być pusta")
    @FutureOrPresent(message = "Data zakończenia nie może być w przeszłości")
    private LocalDate endDate;

    private Boolean insuranceSelected;
    private Boolean gpsSelected;
}
