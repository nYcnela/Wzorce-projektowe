package ma.swiftrent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO prezentujące szczegóły samochodu wraz z zajętymi terminami.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarAvailabilityResponse {

    private CarResponse car;
    private List<RentalResponse> occupiedDates;
    private int occupiedPeriods;
}
