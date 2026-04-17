# ResponseFactory<E, R>

## Rola
`ResponseFactory` to abstrakcyjna baza dla fabryk mapujących encje na DTO.
Nie tworzy konkretnego DTO sama, tylko wymusza implementację metody `create(...)` w podklasach.

## Co robi
1. Definiuje metodę fabryczną `create(E entity)`.
2. Udostępnia gotową metodę `createAll(List<E> entities)`, która mapuje całą listę.

## Wejście i wyjście
- Wejście: encja typu `E` lub lista encji `List<E>`.
- Wyjście: obiekt DTO typu `R` lub lista `List<R>`.

## Krok po kroku
1. Serwis wywołuje `createAll(...)`.
2. `createAll(...)` iteruje po liście.
3. Dla każdego elementu wywoływane jest `this.create(...)`.
4. Konkretna podklasa decyduje jak zbudować DTO.
5. Zwracana jest gotowa lista DTO.

## Czego używa
- Java Stream API (`map`, `collect`).
- Metody `create(...)` dostarczonej przez klasy dziedziczące.

## Gdzie jest używana
Przez:
- `CarResponseFactory`
- `RentalResponseFactory`
- `UserResponseFactory`
