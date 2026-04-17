# RentalResponseFactory

## Rola
Konwertuje encję `Rental` do DTO `RentalResponse`.

## Co robi
Implementuje `create(Rental entity)` odziedziczone po `ResponseFactory<Rental, RentalResponse>`.

## Wejście i wyjście
- Wejście: `Rental`.
- Wyjście: `RentalResponse`.

## Krok po kroku
1. `RentalService` przekazuje encję `Rental`.
2. Fabryka wywołuje `RentalResponse.fromEntity(entity)`.
3. Wynik wraca jako `RentalResponse`.
4. Dla list używane jest `createAll(...)` z klasy bazowej.

## Czego używa
- `RentalResponse.fromEntity(...)`.
- `ResponseFactory.createAll(...)` dla mapowania list.

## Gdzie jest używana
- `RentalService.createRental(...)`.
- `RentalService.getUserRentals()`.
- `RentalService.getAllRentals()`.
- `RentalService.getOccupiedDates(...)`.
