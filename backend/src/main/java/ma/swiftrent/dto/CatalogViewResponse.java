package ma.swiftrent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO prezentujące widok katalogu samochodów.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogViewResponse {

    private List<CarResponse> cars;
    private List<CarResponse> favoriteCars;
    private int totalCars;
    private int favoriteCount;
}
