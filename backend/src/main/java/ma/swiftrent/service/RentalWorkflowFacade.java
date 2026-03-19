package ma.swiftrent.service;

import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.RentalPackageSummaryResponse;
import ma.swiftrent.dto.RentalRequest;
import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.dto.RentalWorkflowOverviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

// Tydzień 4, Wzorzec Facade 3
// Proces wypożyczenia jest udostępniony kontrolerowi przez jeden uproszczony interfejs.
// Fasada grupuje operacje cyklu życia rezerwacji oraz zbiorczy podgląd procesu dla samochodu.
@Service
@RequiredArgsConstructor
public class RentalWorkflowFacade {

    private final RentalService rentalService;

    public RentalResponse createRental(RentalRequest request) {
        return rentalService.createRental(request);
    }

    public void returnRental(Long id) {
        rentalService.returnRental(id);
    }

    public void cancelRental(Long id) {
        rentalService.cancelRental(id);
    }

    public List<RentalResponse> getUserRentals() {
        return rentalService.getUserRentals();
    }

    public List<RentalResponse> getAllRentals() {
        return rentalService.getAllRentals();
    }

    public List<RentalResponse> getOccupiedDates(Long carId) {
        return rentalService.getOccupiedDates(carId);
    }

    public RentalWorkflowOverviewResponse getWorkflowOverview(Long carId) {
        List<RentalResponse> userRentals = rentalService.getUserRentals();
        List<RentalResponse> occupiedDates = rentalService.getOccupiedDates(carId);

        return RentalWorkflowOverviewResponse.builder()
                .carId(carId)
                .userRentals(userRentals)
                .occupiedDates(occupiedDates)
                .userRentalCount(userRentals.size())
                .occupiedPeriods(occupiedDates.size())
                .build();
    }

    public RentalPackageSummaryResponse getPremiumPackageSummary(double carPricePerDay, int days) {
        return rentalService.getPremiumPackageSummary(carPricePerDay, days);
    }
}
// Koniec, Tydzień 4, Wzorzec Facade 3
