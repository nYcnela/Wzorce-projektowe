# Wzorzec Factory Method — opis i implementacja w projekcie SwiftRent

---

## 1. Filozofia wzorca

### Cel i motywacja

Factory Method (Metoda Fabryczna) to wzorzec kreacyjny opisany przez Gang of Four (GoF).
Jego główna idea brzmi:

> **„Zdefiniuj interfejs tworzenia obiektu, ale pozwól podklasom zdecydować, jaką klasę zainstancjować."**

Innymi słowy: klasa bazowa (Creator) zawiera logikę operującą na produkcie, ale
**nie tworzy produktu samodzielnie** — deleguje tę odpowiedzialność do podklas (ConcreteCreator).

### Problem, który rozwiązuje

Wyobraź sobie serwis, który musi tworzyć różne obiekty odpowiedzi (DTO) na podstawie
encji z bazy danych. Bez wzorca serwis wygląda tak:

```java
// ❌ BEZ wzorca — serwis sam wie jak tworzyć DTO, zna konkretne klasy
CarResponse response = CarResponse.fromEntity(car);
RentalResponse response = RentalResponse.fromEntity(rental);
UserResponse response = UserResponse.fromEntity(user);
```

Problemy tego podejścia:
- serwis jest **ściśle związany** z konkretną klasą DTO,
- zmiana sposobu tworzenia DTO wymaga **edycji serwisu**,
- nie ma możliwości podmiany implementacji bez zmiany kodu wywołującego,
- kod tworzenia DTO jest **rozproszony** w wielu miejscach serwisu.

### Rozwiązanie — Factory Method

Wzorzec wprowadza warstwę pośrednią:

```
Creator (abstrakcja)
  └─ operuje na produkcie przez interfejs
  └─ zawiera metodę fabryczną (abstract) — "stwórz produkt"
  └─ może zawierać logikę szablonową korzystającą z metody fabrycznej

ConcreteCreator (podklasa)
  └─ nadpisuje metodę fabryczną
  └─ decyduje JAKIEGO KONKRETNIE produktu stworzyć
```

### Uczestnicy wzorca (GoF)

| Rola GoF         | Opis                                                                 |
|------------------|----------------------------------------------------------------------|
| **Creator**      | Klasa abstrakcyjna z metodą fabryczną (abstract) i logiką szablonową |
| **ConcreteCreator** | Podklasa nadpisująca metodę fabryczną, decyduje o konkretnym typie  |
| **Product**      | Interfejs / klasa bazowa produktu (co fabryka zwraca)               |
| **ConcreteProduct** | Konkretna klasa tworzona przez ConcreteCreator                   |

### Kluczowa zasada OOP, którą realizuje

Factory Method realizuje zasadę **Otwarte/Zamknięte (OCP)** z SOLID:
- klasa bazowa jest **zamknięta** na modyfikacje,
- system jest **otwarty** na rozszerzenie — nowy typ produktu = nowa podklasa,
- **nie trzeba zmieniać** żadnego istniejącego kodu.

---

## 2. Implementacja w projekcie SwiftRent

### Kontekst biznesowy

W SwiftRent backend zwraca dane do frontendu w postaci obiektów DTO (Data Transfer Object):
- `CarResponse` — dane samochodu,
- `RentalResponse` — dane wypożyczenia,
- `UserResponse` — dane użytkownika.

Każdy serwis (`CarService`, `RentalService`, `UserService`) musi konwertować
encje JPA na odpowiednie DTO. To właśnie to miejsce wykorzystuje Factory Method.

---

### Struktura klas w projekcie

```
ResponseFactory<E, R>          ← Creator (abstrakcja)
    + abstract R create(E)     ← metoda fabryczna (do nadpisania)
    + List<R> createAll(List)  ← metoda szablonowa (logika wspólna)
         │
         ├── CarResponseFactory     ← ConcreteCreator #1  (Car → CarResponse)
         ├── RentalResponseFactory  ← ConcreteCreator #2  (Rental → RentalResponse)
         └── UserResponseFactory    ← ConcreteCreator #3  (User → UserResponse)

Produkty:
    CarResponse, RentalResponse, UserResponse  ← ConcreteProduct
```

---

### Creator — `ResponseFactory<E, R>`

```java
public abstract class ResponseFactory<E, R> {

    // Metoda fabryczna — każda podklasa definiuje jak stworzyć produkt
    public abstract R create(E entity);

    // Metoda szablonowa — wspólna logika, korzysta z metody fabrycznej
    // Nie wie NIC o konkretnym typie R — operuje tylko na abstrakcji
    public List<R> createAll(List<E> entities) {
        return entities.stream()
                .map(this::create)   // ← wywołuje create() polimorficznie
                .collect(Collectors.toList());
    }
}
```

Klasa jest **generyczna** (`<E, R>`), co pozwala obsłużyć dowolną parę encja–DTO
jednym kodem szablonowym. `createAll()` jest napisane **raz** i działa identycznie
dla wszystkich trzech podklas.

---

### ConcreteCreator #1 — `CarResponseFactory`

```java
@Component
public class CarResponseFactory extends ResponseFactory<Car, CarResponse> {

    @Override
    public CarResponse create(Car entity) {
        return CarResponse.fromEntity(entity);
    }
}
```

**Rola GoF:** ConcreteCreator dla pary `Car` (encja) → `CarResponse` (DTO).
Nadpisuje metodę fabryczną `create()` i deleguje do statycznej metody `fromEntity()`.

**Gdzie używane:** `CarService` i `UserService` (do konwersji ulubionych samochodów).

---

### ConcreteCreator #2 — `RentalResponseFactory`

```java
@Component
public class RentalResponseFactory extends ResponseFactory<Rental, RentalResponse> {

    @Override
    public RentalResponse create(Rental entity) {
        return RentalResponse.fromEntity(entity);
    }
}
```

**Rola GoF:** ConcreteCreator dla pary `Rental` (encja) → `RentalResponse` (DTO).

**Gdzie używane:** `RentalService` — przy tworzeniu rezerwacji, pobieraniu historii
użytkownika, pobieraniu wszystkich rezerwacji (admin) i pobieraniu zajętych terminów.

---

### ConcreteCreator #3 — `UserResponseFactory`

```java
@Component
public class UserResponseFactory extends ResponseFactory<User, UserResponse> {

    @Override
    public UserResponse create(User entity) {
        return UserResponse.fromEntity(entity);
    }
}
```

**Rola GoF:** ConcreteCreator dla pary `User` (encja) → `UserResponse` (DTO).

**Gdzie używane:** `UserService` — przy pobieraniu listy wszystkich użytkowników (admin).

---

### Użycie w serwisach

**`CarService`** — fabryka zastępuje bezpośrednie wywołanie `CarResponse.fromEntity()`:

```java
// Deklaracja fabryki
private final CarResponseFactory carResponseFactory = new CarResponseFactory();

// Użycie w metodzie mapToResponse()
private CarResponse mapToResponse(Car car) {
    CarResponse response = carResponseFactory.create(car);  // ← Factory Method
    // ... dalsza logika (availableFrom)
    return response;
}
```

**`RentalService`** — fabryka obsługuje zarówno konwersję pojedynczą, jak i listy:

```java
private final RentalResponseFactory rentalResponseFactory = new RentalResponseFactory();

// Konwersja pojedynczego obiektu
return rentalResponseFactory.create(savedRental);

// Konwersja całej listy (metoda szablonowa z Creator)
return rentalResponseFactory.createAll(rentalRepository.findAll());
```

**`UserService`** — używa dwóch różnych fabryk w jednej klasie:

```java
private final UserResponseFactory userResponseFactory = new UserResponseFactory();
private final CarResponseFactory carResponseFactory   = new CarResponseFactory();

// Lista użytkowników
return userResponseFactory.createAll(userRepository.findAll());

// Lista ulubionych samochodów użytkownika
return carResponseFactory.createAll(user.getFavorites());
```

---

## 3. Przepływ wywołań — diagram sekwencji

```
RentalService                RentalResponseFactory         RentalResponse
     │                              │                            │
     │  createAll(list)             │                            │
     │─────────────────────────────>│                            │
     │                              │  (dla każdej encji)        │
     │                              │  create(rental)            │
     │                              │─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─>│
     │                              │                    fromEntity(rental)
     │                              │<─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─│
     │  List<RentalResponse>        │                            │
     │<─────────────────────────────│                            │
```

Serwis **nigdy nie wywołuje** `RentalResponse.fromEntity()` bezpośrednio.
Zawsze pośredniczy fabryka — to ona jest odpowiedzialna za tworzenie produktu.

---

## 4. Porównanie z pozostałymi wzorcami w projekcie

| Wzorzec            | Odpowiada na pytanie          | Gdzie w projekcie                        |
|--------------------|-------------------------------|------------------------------------------|
| **Singleton**      | Ile instancji klasy?          | `ApplicationClock`, `SecurityContextAccessor`, `UploadStorageSettings` |
| **Builder**        | Jak zbudować złożony obiekt?  | `Car`, `User`, `Rental` — tworzenie encji |
| **Prototype**      | Jak skopiować istniejący obiekt? | `Car.clone()`, `User.clone()`, `Rental.clone()` |
| **Factory Method** | Kto decyduje jaki obiekt stworzyć? | `CarResponseFactory`, `RentalResponseFactory`, `UserResponseFactory` |
