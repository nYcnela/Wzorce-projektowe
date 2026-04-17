# ReturnRentalActionFactory

## Rola
Fabryka tworząca akcję zwrotu wypożyczenia.

## Co robi
Implementuje `createAction()` i zwraca akcję `ReturnRentalAction`.
Ta akcja aktualizuje dane wypożyczenia po zwrocie auta.

## Wejście i wyjście
- Wejście konstruktora: `ApplicationClock`.
- Wyjście `createAction()`: `RentalStatusAction` (konkretnie ReturnRentalAction).

## Krok po kroku
1. `RentalService.returnRental(...)` tworzy `ReturnRentalActionFactory(applicationClock)`.
2. Serwis wywołuje `process(rental)` z klasy bazowej.
3. Fabryka tworzy `ReturnRentalAction`.
4. Akcja ustawia:
   - `endDate` na dzisiaj,
   - `totalCost` przeliczony po realnych dniach,
   - status wypożyczenia na `COMPLETED`,
   - status auta na `AVAILABLE`.
5. `RentalService` zapisuje zmienione encje.

## Czego używa
- `ApplicationClock.today()`.
- `ChronoUnit.DAYS` do obliczenia liczby dni.
- pól `Rental` i `Car` (`status`, `endDate`, `totalCost`).

## Gdzie jest używana
- `RentalService.returnRental(...)`.
