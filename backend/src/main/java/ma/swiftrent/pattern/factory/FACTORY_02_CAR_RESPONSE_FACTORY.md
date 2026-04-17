# CarResponseFactory

## Rola
Konwertuje encję `Car` do DTO `CarResponse`.

## Co robi
Implementuje `create(Car entity)` odziedziczone po `ResponseFactory<Car, CarResponse>`.

## Wejście i wyjście
- Wejście: `Car`.
- Wyjście: `CarResponse`.

## Krok po kroku
1. Serwis przekazuje obiekt `Car`.
2. Fabryka wywołuje `CarResponse.fromEntity(entity)`.
3. Zwracany jest gotowy `CarResponse`.

## Czego używa
- `CarResponse.fromEntity(...)`.
- Generycznej klasy bazowej `ResponseFactory`.

## Gdzie jest używana
- `CarService` (mapowanie samochodu do odpowiedzi API).
- `UserService` (mapowanie listy ulubionych aut użytkownika).
