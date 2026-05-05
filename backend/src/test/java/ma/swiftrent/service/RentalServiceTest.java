package ma.swiftrent.service;

import ma.swiftrent.composite.rentalPackage.RentalPackage;
import ma.swiftrent.dto.RentalPackageSummaryResponse;
import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.entity.Rental;
import ma.swiftrent.entity.User;
import ma.swiftrent.pattern.factory.RentalResponseFactory;
import ma.swiftrent.pattern.observer.rentalcreated.RentalCreatedSubject;
import ma.swiftrent.pattern.observer.rentalstatus.RentalStatusChangedSubject;
import ma.swiftrent.pattern.singleton.SecurityContextAccessor;
import ma.swiftrent.repository.CarRepository;
import ma.swiftrent.repository.RentalRepository;
import ma.swiftrent.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/*
    Tydzień 13, Testy jednostkowe 2
    Tutaj mamy testy jednostkowe dla metod z klasy RentalService
    Wykorzystano adnotację @BeforeEach, aby inicjalizację niezbędnych elementów wykonać przed każdym
    testem, bez niepotrzebnego powtarzania kodu
 */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentalCreatedSubject rentalCreatedSubject;

    @Mock
    private RentalStatusChangedSubject rentalStatusChangedSubject;

    @Mock
    private RentalResponseFactory rentalResponseFactory;

    @Mock
    private SecurityContextAccessor securityContextAccessor;

    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalService = new RentalService(
                rentalRepository,
                carRepository,
                userRepository,
                rentalCreatedSubject,
                rentalStatusChangedSubject
        );

        ReflectionTestUtils.setField(
                rentalService,
                "rentalResponseFactory",
                rentalResponseFactory
        );

        ReflectionTestUtils.setField(
                rentalService,
                "securityContextAccessor",
                securityContextAccessor
        );
    }

    @Test
    void shouldCreatePremiumPackage() {
        double carPricePerDay = 100.0;
        int days = 3;

        RentalPackage result = rentalService.createPremiumPackage(carPricePerDay, days);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Pakiet Premium");
    }

    @Test
    void shouldCreatePremiumPackageSummary() {
        double carPricePerDay = 100.0;
        int days = 3;

        RentalPackageSummaryResponse result =
                rentalService.getPremiumPackageSummary(carPricePerDay, days);

        assertThat(result).isNotNull();
        assertThat(result.getPackageName()).isEqualTo("Pakiet Premium");
        assertThat(result.getPackageCount()).isEqualTo(1);
        assertThat(result.getServiceItemCount()).isEqualTo(3);
        assertThat(result.getDescription()).isNotBlank();
    }

    @Test
    void shouldReturnAllRentals() {
        Rental rental = mock(Rental.class);
        RentalResponse rentalResponse = mock(RentalResponse.class);

        List<Rental> rentals = List.of(rental);
        List<RentalResponse> expectedResponses = List.of(rentalResponse);

        when(rentalRepository.findAll()).thenReturn(rentals);
        when(rentalResponseFactory.createAll(rentals)).thenReturn(expectedResponses);

        List<RentalResponse> result = rentalService.getAllRentals();

        assertThat(result).isEqualTo(expectedResponses);

        verify(rentalRepository).findAll();
        verify(rentalResponseFactory).createAll(rentals);
    }

    @Test
    void shouldReturnUserRentals() {
        String email = "user@test.com";

        User user = mock(User.class);
        Rental rental = mock(Rental.class);
        RentalResponse rentalResponse = mock(RentalResponse.class);

        List<Rental> rentals = List.of(rental);
        List<RentalResponse> expectedResponses = List.of(rentalResponse);

        when(securityContextAccessor.getCurrentUserEmail()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(1L);
        when(rentalRepository.findByUserId(1L)).thenReturn(rentals);
        when(rentalResponseFactory.createAll(rentals)).thenReturn(expectedResponses);

        List<RentalResponse> result = rentalService.getUserRentals();

        assertThat(result).isEqualTo(expectedResponses);

        verify(securityContextAccessor).getCurrentUserEmail();
        verify(userRepository).findByEmail(email);
        verify(rentalRepository).findByUserId(1L);
        verify(rentalResponseFactory).createAll(rentals);
    }

    @Test
    void shouldThrowExceptionWhenCurrentUserDoesNotExist() {
        String email = "missing@test.com";

        when(securityContextAccessor.getCurrentUserEmail()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentalService.getUserRentals())
                .isInstanceOf(RentalService.UserNotFoundException.class)
                .hasMessageContaining(email);

        verify(securityContextAccessor).getCurrentUserEmail();
        verify(userRepository).findByEmail(email);
        verifyNoInteractions(rentalRepository);
        verifyNoInteractions(rentalResponseFactory);
    }
}
//Koniec, Tydzień 13, Testy jednostkowe 2