# CancelRentalActionFactory

## Rola
Fabryka tworząca akcję anulowania wypożyczenia.

## Co robi
Implementuje `createAction()` i zwraca akcję `CancelRentalAction`.
Ta akcja wykonuje anulowanie bez opłaty.

## Wejście i wyjście
- Wejście: brak dodatkowych zależności.
- Wyjście `createAction()`: `RentalStatusAction` (konkretnie CancelRentalAction).

## Krok po kroku
1. `RentalService.cancelRental(...)` kończy walidacje terminu i uprawnień.
2. Tworzy `CancelRentalActionFactory`.
3. Wywołuje `process(rental)`.
4. Fabryka tworzy `CancelRentalAction`.
5. Akcja ustawia:
   - status na `CANCELLED`,
   - `totalCost` na `0`.
6. `RentalService` zapisuje encję `Rental`.

## Czego używa
- pól encji `Rental` (`status`, `totalCost`).
- interfejsu `RentalStatusAction`.

## Gdzie jest używana
- `RentalService.cancelRental(...)`.
