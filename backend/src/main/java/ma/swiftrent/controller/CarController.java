package ma.swiftrent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * Kontroler obsługujący operacje na samochodach.
 */
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    /**
     * Endpoint pobierający wszystkie samochody (dostępny publicznie).
     *
     * @param sortBy Opcjonalny parametr sortowania: price-asc, price-desc
     * @return Lista wszystkich samochodów
     */
    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars(
            @RequestParam(required = false) String sortBy
    ) {
        return ResponseEntity.ok(carService.getAllCars(sortBy));
    }

    /**
     * Endpoint pobierający samochód po ID (dostępny publicznie).
     *
     * @param id ID samochodu
     * @return Dane samochodu
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    /**
     * Endpoint tworzący nowy samochód (tylko dla ADMIN).
     *
     * @param request Dane nowego samochodu
     * @param image Zdjęcie samochodu
     * @return Utworzony samochód
     */
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })//body zadania musi byc multipart/form-data bo sa dane tekstowe jak i obraz
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponse> createCar(
            @RequestPart("car") @Valid CarRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar(request, image));
    }

    /**
     * Endpoint aktualizujący dane samochodu (tylko dla ADMIN).
     *
     * @param id ID samochodu
     * @param request Nowe dane samochodu
     * @param image Nowe zdjęcie samochodu (opcjonalne)
     * @return Zaktualizowany samochód
     */
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }) //body zadania musi byc multipart/form-data bo sa dane tekstowe jak i obraz
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable Long id,
            @RequestPart("car") @Valid CarRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(carService.updateCar(id, request, image));
    }

    /**
     * Endpoint usuwający samochód (tylko dla ADMIN).
     *
     * @param id ID samochodu do usunięcia
     * @return Odpowiedź bez zawartości
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint do duplikowania samochodów (tylko dla ADMIN)
     * @param id ID samochodu do zduplikowania
     * @return Odpowiedź z id duplikatu
     */
    @PostMapping("/{id}/duplicate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponse> duplicateCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.duplicateCar(id));
    }
}
