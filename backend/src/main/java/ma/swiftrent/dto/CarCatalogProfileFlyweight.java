package ma.swiftrent.dto;

import ma.swiftrent.entity.Car;

import java.math.BigDecimal;

// Tydzien 4, Wzorzec Flyweight 3
// W katalogu wiele odpowiedzi moze wspoldzielic ten sam opis modelu samochodu.
// Flyweight przechowuje niezmienne dane prezentacyjne auta, a dane konkretnej oferty sa przekazywane z zewnatrz.
public final class CarCatalogProfileFlyweight {

    private final String brand;
    private final String model;
    private final Integer productionYear;
    private final String color;
    private final String imageUrl;

    CarCatalogProfileFlyweight(
            String brand,
            String model,
            Integer productionYear,
            String color,
            String imageUrl
    ) {
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    public CarResponse toResponse(Long id, BigDecimal pricePerDay, Car.CarStatus status) {
        return CarResponse.builder()
                .id(id)
                .brand(brand)
                .model(model)
                .pricePerDay(pricePerDay)
                .productionYear(productionYear)
                .color(color)
                .imageUrl(imageUrl)
                .status(status.name())
                .available(status == Car.CarStatus.AVAILABLE)
                .build();
    }
}
// Koniec, Tydzien 4, Wzorzec Flyweight 3
