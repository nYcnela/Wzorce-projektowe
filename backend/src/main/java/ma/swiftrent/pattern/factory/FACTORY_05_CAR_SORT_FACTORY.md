# CarSortFactory

## Rola
Abstrakcyjna baza dla fabryk tworzących komparator sortowania samochodów.

## Co robi
1. Definiuje metodę fabryczną `createComparator()`.
2. Udostępnia metodę `forStrategy(sortBy)`, która wybiera konkretną fabrykę.

## Wejście i wyjście
- Wejście: tekst strategii `sortBy` (`price-asc`, `price-desc`, inne/null).
- Wyjście: fabryka konkretna, a finalnie `Comparator<CarResponse>`.

## Krok po kroku
1. `CarService` przekazuje `sortBy`.
2. `forStrategy(sortBy)` zwraca odpowiednią fabrykę:
   - `PriceAscSortFactory`
   - `PriceDescSortFactory`
   - `DefaultSortFactory`
3. Serwis wywołuje `createComparator()`.
4. Otrzymany komparator sortuje listę `CarResponse`.

## Czego używa
- `Comparator<CarResponse>`.
- Podklas konkretnych wybieranych przez `switch`.

## Gdzie jest używana
- `CarService.getAllCars(String sortBy)`.
