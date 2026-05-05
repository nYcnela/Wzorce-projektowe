package ma.swiftrent.controller;

import ma.swiftrent.dto.AdminFleetOverviewResponse;
import ma.swiftrent.dto.CarAvailabilityResponse;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.service.AdminFleetFacade;
import ma.swiftrent.service.CatalogFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/*
    Tydzień 13, Testy jednostkowe 1
    Testy jednostkowe dla kontrolera CarController,
    które sprawdzają różne endpointy
 */
@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CatalogFacade catalogFacade;

    @Mock
    private AdminFleetFacade adminFleetFacade;

    @InjectMocks
    private CarController carController;

    @Test
    void shouldReturnAllCars() {
        String sortBy = "price-asc";
        List<CarResponse> cars = List.of(mock(CarResponse.class));

        when(catalogFacade.getPublicCatalog(sortBy)).thenReturn(cars);

        ResponseEntity<List<CarResponse>> response = carController.getAllCars(sortBy);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(cars);

        verify(catalogFacade).getPublicCatalog(sortBy);
    }

    @Test
    void shouldReturnCarById() {
        Long carId = 1L;
        CarResponse carResponse = mock(CarResponse.class);

        when(catalogFacade.getCarDetails(carId)).thenReturn(carResponse);

        ResponseEntity<CarResponse> response = carController.getCarById(carId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(carResponse);

        verify(catalogFacade).getCarDetails(carId);
    }

    @Test
    void shouldReturnCarAvailability() {
        Long carId = 1L;
        CarAvailabilityResponse availabilityResponse = mock(CarAvailabilityResponse.class);

        when(catalogFacade.getCarAvailability(carId)).thenReturn(availabilityResponse);

        ResponseEntity<CarAvailabilityResponse> response =
                carController.getCarAvailability(carId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(availabilityResponse);

        verify(catalogFacade).getCarAvailability(carId);
    }

    @Test
    void shouldDeleteCar() {
        Long carId = 1L;

        ResponseEntity<Void> response = carController.deleteCar(carId);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.getBody()).isNull();

        verify(adminFleetFacade).deleteCar(carId);
    }

    @Test
    void shouldDuplicateCar() {
        Long carId = 1L;
        CarResponse duplicatedCar = mock(CarResponse.class);

        when(adminFleetFacade.duplicateCar(carId)).thenReturn(duplicatedCar);

        ResponseEntity<CarResponse> response = carController.duplicateCar(carId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(duplicatedCar);

        verify(adminFleetFacade).duplicateCar(carId);
    }
}
// Koniec, Tydzień 13, Testy jednostkowe 1