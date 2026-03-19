package ma.swiftrent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalPackageSummaryResponse {

    private String packageName;
    private double totalPrice;
    private String description;
    private int packageCount;
    private int serviceItemCount;
}
