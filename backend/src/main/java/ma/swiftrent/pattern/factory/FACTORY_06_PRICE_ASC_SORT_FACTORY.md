# PriceAscSortFactory

## Rola
Konkretna fabryka sortowania rosnącego po cenie.

## Co robi
Implementuje `createComparator()` i zwraca komparator po `pricePerDay` rosnąco.

## Wejście i wyjście
- Wejście: brak (poza wywołaniem metody).
- Wyjście: `Comparator<CarResponse>`.

## Krok po kroku
1. `CarSortFactory.forStrategy("price-asc")` wybiera tę fabrykę.
2. `createComparator()` zwraca `Comparator.comparing(CarResponse::getPricePerDay)`.
3. `CarService` używa go w `cars.sort(...)`.

## Czego używa
- Getter `CarResponse.getPricePerDay()`.
- API `Comparator.comparing(...)`.

## Gdzie jest używana
Pośrednio przez:
- `CarService.getAllCars(...)` dla `sortBy=price-asc`.
