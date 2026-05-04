package ma.swiftrent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.swiftrent.dto.AdminFleetOverviewResponse;
import ma.swiftrent.dto.CarAvailabilityResponse;
import ma.swiftrent.dto.CarRequest;
import ma.swiftrent.dto.CarResponse;
import ma.swiftrent.service.AdminFleetFacade;
import ma.swiftrent.service.CatalogFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Kontroler obsługujący operacje na samochodach.
 */
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CatalogFacade catalogFacade;
    private final AdminFleetFacade adminFleetFacade;

    /**
     * Endpoint pobierający wszystkie samochody (dostępny publicznie).
     *
     * @param sortBy Opcjonalny parametr sortowania: price-asc, price-desc
     * @return Lista wszystkich samochodów
     */
    /*
    Tydzień 9, Jedna rola funkcji 2
    Ta funkcja odpowiada tylko za obsługę endpointu pobierającego wszystkie samochody
     */
    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars(
            @RequestParam(required = false) String sortBy
    ) {
        return ResponseEntity.ok(catalogFacade.getPublicCatalog(sortBy));
    }
    //Koniec, Tydzień 9, Jedna rola funkcji 2

    /**
     * Endpoint pobierający samochód po ID (dostępny publicznie).
     *
     * @param id ID samochodu
     * @return Dane samochodu
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogFacade.getCarDetails(id));
    }

    /**
     * Endpoint pobierający dostępność samochodu wraz z zajętymi terminami.
     *
     * @param id ID samochodu
     * @return Szczegóły samochodu i lista zajętych terminów
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<CarAvailabilityResponse> getCarAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(catalogFacade.getCarAvailability(id));
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
        return ResponseEntity.status(HttpStatus.CREATED).body(adminFleetFacade.createCar(request, image));
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
        return ResponseEntity.ok(adminFleetFacade.updateCar(id, request, image));
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
        adminFleetFacade.deleteCar(id);
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
        return ResponseEntity.ok(adminFleetFacade.duplicateCar(id));
    }

    /**
     * Endpoint pobierający podsumowanie floty dla administratora.
     *
     * @param sortBy Opcjonalny parametr sortowania
     * @return Zbiorczy widok floty
     */
    @GetMapping("/admin/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminFleetOverviewResponse> getAdminFleetOverview(
            @RequestParam(required = false) String sortBy
    ) {
        return ResponseEntity.ok(adminFleetFacade.getFleetOverview(sortBy));
    }
}
