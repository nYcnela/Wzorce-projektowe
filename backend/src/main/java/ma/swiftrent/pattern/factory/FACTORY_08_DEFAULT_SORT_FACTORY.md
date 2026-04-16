# DefaultSortFactory

## Rola
Fabryka domyślna dla sortowania samochodów, gdy `sortBy` jest puste lub nieznane.

## Co robi
Implementuje `createComparator()` i zwraca komparator po `id`.

## Wejście i wyjście
- Wejście: brak.
- Wyjście: `Comparator<CarResponse>`.

## Krok po kroku
1. `CarSortFactory.forStrategy(sortBy)` nie rozpoznaje strategii.
2. Zwracana jest `DefaultSortFactory`.
3. `createComparator()` zwraca sortowanie po `id`.
4. `CarService` ma stabilny, przewidywalny porządek wyników.

## Czego używa
- `CarResponse.getId()`.
- `Comparator.comparing(...)`.

## Gdzie jest używana
Pośrednio przez:
- `CarService.getAllCars(...)` dla `sortBy=null` lub nieobsługiwanej wartości.
