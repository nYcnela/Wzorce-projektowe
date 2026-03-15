package ma.swiftrent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO prezentujące zbiorczy widok procesu wypożyczeń dla wybranego samochodu.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalWorkflowOverviewResponse {

    private Long carId;
    private List<RentalResponse> userRentals;
    private List<RentalResponse> occupiedDates;
    private int userRentalCount;
    private int occupiedPeriods;
}
