package ma.swiftrent.service;

import ma.swiftrent.dto.RentalPackageSummaryResponse;
import ma.swiftrent.dto.RentalRequest;
import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.dto.RentalWorkflowOverviewResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
/*
    Tydzień 13, Testy jednostkowe 4
    Testy jednostkowe dla RentalWorkflowFacade, które schodzi o poziom niżej od RentalService
    więc mamy testy jednostkowe dla ogólnie usług wypożyczeniowych w programie
    oraz dla bardziej szczegółowych metod
 */
@ExtendWith(MockitoExtension.class)
class RentalWorkflowFacadeTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalWorkflowFacade rentalWorkflowFacade;

    @Test
    void shouldCreateRental() {
        RentalRequest request = mock(RentalRequest.class);
        RentalResponse expectedResponse = mock(RentalResponse.class);

        when(rentalService.createRental(request)).thenReturn(expectedResponse);

        RentalResponse result = rentalWorkflowFacade.createRental(request);

        assertThat(result).isEqualTo(expectedResponse);

        verify(rentalService).createRental(request);
    }

    @Test
    void shouldReturnRental() {
        Long rentalId = 1L;

        rentalWorkflowFacade.returnRental(rentalId);

        verify(rentalService).returnRental(rentalId);
    }

    @Test
    void shouldCancelRental() {
        Long rentalId = 1L;

        rentalWorkflowFacade.cancelRental(rentalId);

        verify(rentalService).cancelRental(rentalId);
    }

    @Test
    void shouldCreateWorkflowOverview() {
        Long carId = 10L;

        RentalResponse userRental = mock(RentalResponse.class);
        RentalResponse occupiedDate = mock(RentalResponse.class);

        List<RentalResponse> userRentals = List.of(userRental);
        List<RentalResponse> occupiedDates = List.of(occupiedDate);

        when(rentalService.getUserRentals()).thenReturn(userRentals);
        when(rentalService.getOccupiedDates(carId)).thenReturn(occupiedDates);

        RentalWorkflowOverviewResponse result =
                rentalWorkflowFacade.getWorkflowOverview(carId);

        assertThat(result).isNotNull();
        assertThat(result.getCarId()).isEqualTo(carId);
        assertThat(result.getUserRentals()).isEqualTo(userRentals);
        assertThat(result.getOccupiedDates()).isEqualTo(occupiedDates);
        assertThat(result.getUserRentalCount()).isEqualTo(1);
        assertThat(result.getOccupiedPeriods()).isEqualTo(1);

        verify(rentalService).getUserRentals();
        verify(rentalService).getOccupiedDates(carId);
    }

    @Test
    void shouldReturnPremiumPackageSummary() {
        double carPricePerDay = 100.0;
        int days = 3;

        RentalPackageSummaryResponse expectedResponse =
                mock(RentalPackageSummaryResponse.class);

        when(rentalService.getPremiumPackageSummary(carPricePerDay, days))
                .thenReturn(expectedResponse);

        RentalPackageSummaryResponse result =
                rentalWorkflowFacade.getPremiumPackageSummary(carPricePerDay, days);

        assertThat(result).isEqualTo(expectedResponse);

        verify(rentalService).getPremiumPackageSummary(carPricePerDay, days);
    }
}
//Koniec, Tydzień 13, Testy jednostkowe 4