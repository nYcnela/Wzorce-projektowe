package ma.swiftrent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.swiftrent.entity.Rental;
import ma.swiftrent.pattern.singleton.ApplicationClock;
import ma.swiftrent.pattern.state.rentaltimeline.RentalTimelineState;
import ma.swiftrent.pattern.state.rentaltimeline.RentalTimelineStateContext;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dla odpowiedzi z danymi wypożyczenia.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private Long carId;
    private String carBrand;
    private String carModel;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private String status;
    private String timelineState;
    private boolean cancellableToday;
    private boolean ongoing;

    /**
     * Konwertuje encję Rental na RentalResponse.
     */
    public static RentalResponse fromEntity(Rental rental) {
        // Tydzień 6, Wzorzec State 3 – użycie RentalTimelineStateContext (Context)
        RentalTimelineState timelineState = new RentalTimelineStateContext()
                .resolve(rental, ApplicationClock.getInstance());

        return RentalResponse.builder()
                .id(rental.getId())
                .userId(rental.getUser() != null ? rental.getUser().getId() : null)
                .userEmail(rental.getUser() != null ? rental.getUser().getEmail() : "Użytkownik usunięty")
                .carId(rental.getCar().getId())
                .carBrand(rental.getCar().getBrand())
                .carModel(rental.getCar().getModel())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .totalCost(rental.getTotalCost())
                .status(rental.getStatus().name())
                .timelineState(timelineState.getLabel())
                .cancellableToday(timelineState.canBeCancelledToday())
                .ongoing(timelineState.isOngoing())
                .build();
    }
}
