package ma.swiftrent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.RentalPackageSummaryResponse;
import ma.swiftrent.dto.RentalRequest;
import ma.swiftrent.dto.RentalResponse;
import ma.swiftrent.dto.RentalWorkflowOverviewResponse;
import ma.swiftrent.service.RentalWorkflowFacade;
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

    private final RentalWorkflowFacade rentalWorkflowFacade;

    @PostMapping
    public ResponseEntity<RentalResponse> createRental(@Valid @RequestBody RentalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalWorkflowFacade.createRental(request));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnRental(@PathVariable Long id) {
        rentalWorkflowFacade.returnRental(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRental(@PathVariable Long id) {
        rentalWorkflowFacade.cancelRental(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-rentals")
    public ResponseEntity<List<RentalResponse>> getUserRentals() {
        return ResponseEntity.ok(rentalWorkflowFacade.getUserRentals());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        return ResponseEntity.ok(rentalWorkflowFacade.getAllRentals());
    }

    @GetMapping("/car/{carId}/occupied")
    public ResponseEntity<List<RentalResponse>> getOccupiedDates(@PathVariable Long carId) {
        return ResponseEntity.ok(rentalWorkflowFacade.getOccupiedDates(carId));
    }

    @GetMapping("/overview/car/{carId}")
    public ResponseEntity<RentalWorkflowOverviewResponse> getWorkflowOverview(@PathVariable Long carId) {
        return ResponseEntity.ok(rentalWorkflowFacade.getWorkflowOverview(carId));
    }

    @GetMapping("/premium-package/summary")
    public ResponseEntity<RentalPackageSummaryResponse> getPremiumPackageSummary(
            @RequestParam double carPricePerDay,
            @RequestParam int days
    ) {
        return ResponseEntity.ok(rentalWorkflowFacade.getPremiumPackageSummary(carPricePerDay, days));
    }
}
