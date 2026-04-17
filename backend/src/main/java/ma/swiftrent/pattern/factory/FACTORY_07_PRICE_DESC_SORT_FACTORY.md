# PriceDescSortFactory

## Rola
Konkretna fabryka sortowania malejącego po cenie.

## Co robi
Implementuje `createComparator()` i zwraca komparator po `pricePerDay` malejąco.

## Wejście i wyjście
- Wejście: brak.
- Wyjście: `Comparator<CarResponse>`.

## Krok po kroku
1. `CarSortFactory.forStrategy("price-desc")` wybiera tę fabrykę.
2. `createComparator()` zwraca komparator `comparing(...).reversed()`.
3. Serwis sortuje listę od najdroższego do najtańszego.

## Czego używa
- `Comparator.comparing(...)`.
- `Comparator.reversed()`.

## Gdzie jest używana
Pośrednio przez:
- `CarService.getAllCars(...)` dla `sortBy=price-desc`.
