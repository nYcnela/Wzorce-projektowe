# RentalStatusActionFactory

## Rola
Abstrakcyjna baza dla fabryk tworzących akcje zmiany statusu wypożyczenia.

## Co robi
1. Definiuje metodę fabryczną `createAction()`.
2. Definiuje metodę `process(Rental rental)`, która:
   - tworzy akcję,
   - uruchamia ją na obiekcie `Rental`.

## Wejście i wyjście
- Wejście: `Rental` (dla `process`).
- Wyjście: `RentalStatusAction` (dla `createAction`), a `process` zwraca `void`.

## Krok po kroku
1. `RentalService` wybiera konkretną fabrykę (zwrot/anulowanie).
2. Wywołuje `process(rental)`.
3. `process` uruchamia `createAction()`.
4. Zwrócona akcja wykonuje `execute(rental)` i modyfikuje stan encji.

## Czego używa
- Interfejsu `RentalStatusAction`.
- Konkretnych fabryk: `ReturnRentalActionFactory`, `CancelRentalActionFactory`.

## Gdzie jest używana
- `RentalService.returnRental(...)`.
- `RentalService.cancelRental(...)`.
