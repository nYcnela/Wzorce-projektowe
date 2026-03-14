package ma.swiftrent.dto;

import ma.swiftrent.entity.Car;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CarCatalogProfileFactory {

    private static final Map<CarCatalogProfileKey, CarCatalogProfileFlyweight> CATALOG_PROFILES =
            new ConcurrentHashMap<>();

    private CarCatalogProfileFactory() {
    }

    public static CarCatalogProfileFlyweight forCar(Car car) {
        CarCatalogProfileKey key = new CarCatalogProfileKey(
                car.getBrand(),
                car.getModel(),
                car.getProductionYear(),
                car.getColor(),
                car.getImageUrl()
        );

        return CATALOG_PROFILES.computeIfAbsent(
                key,
                ignored -> new CarCatalogProfileFlyweight(
                        car.getBrand(),
                        car.getModel(),
                        car.getProductionYear(),
                        car.getColor(),
                        car.getImageUrl()
                )
        );
    }

    private record CarCatalogProfileKey(
            String brand,
            String model,
            Integer productionYear,
            String color,
            String imageUrl
    ) {
    }
}
