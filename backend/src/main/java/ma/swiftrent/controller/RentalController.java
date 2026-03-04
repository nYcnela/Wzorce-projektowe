package ma.swiftrent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.RentalRequest;
import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler obsługujący operacje na wypożyczeniach.
 */
@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    /**
     * Endpoint tworzący nowe wypożyczenie (dostępny dla zalogowanych użytkowników).
     *
     * @param request Dane wypożyczenia
     * @return Utworzone wypożyczenie
     */
    @PostMapping
    public ResponseEntity<RentalResponse> createRental(@Valid @RequestBody RentalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.createRental(request));
    }

    /**
     * Endpoint zwracający samochód.
     *
     * @param id ID wypożyczenia
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnRental(@PathVariable Long id) {
        rentalService.returnRental(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint anulujący rezerwację (tylko przed datą rozpoczęcia).
     *
     * @param id ID wypożyczenia
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRental(@PathVariable Long id) {
        rentalService.cancelRental(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint pobierający wypożyczenia zalogowanego użytkownika.
     *
     * @return Lista wypożyczeń użytkownika
     */
    @GetMapping("/my-rentals")
    public ResponseEntity<List<RentalResponse>> getUserRentals() {
        return ResponseEntity.ok(rentalService.getUserRentals());
    }

    /**
     * Endpoint pobierający wszystkie wypożyczenia (tylko dla ADMIN).
     *
     * @return Lista wszystkich wypożyczeń
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    /**
     * Endpoint pobierający zajęte terminy dla danego samochodu.
     */
    @GetMapping("/car/{carId}/occupied")
    public ResponseEntity<List<RentalResponse>> getOccupiedDates(@PathVariable Long carId) {
        return ResponseEntity.ok(rentalService.getOccupiedDates(carId));
    }
}
