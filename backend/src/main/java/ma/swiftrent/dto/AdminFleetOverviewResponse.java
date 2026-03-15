package ma.swiftrent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO prezentujące widok floty dla administratora.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminFleetOverviewResponse {

    private List<CarResponse> cars;
    private long totalCars;
    private long availableCars;
    private long unavailableCars;
}
