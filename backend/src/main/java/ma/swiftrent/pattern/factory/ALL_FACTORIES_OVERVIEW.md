# Wszystkie fabryki użyte w projekcie SwiftRent

Ten dokument zbiera wszystkie implementacje factory/factory method użyte w backendzie projektu.
Opisuje:
- co dana fabryka robi,
- co zwraca,
- gdzie jest używana,
- jaki problem rozwiązuje.

---

## 1. Fabryki mapowania encji na DTO

### Klasa bazowa

- ResponseFactory<E, R>

Rola:
- Abstrakcyjna klasa bazowa dla konwersji Encja -> DTO.

Najważniejsze metody:
- create(E entity): metoda fabryczna, implementowana w podklasach.
- createAll(List<E> entities): metoda pomocnicza mapująca całą listę przez create(...).

Co zwraca:
- Pojedynczy obiekt typu R (w create).
- Listę obiektów typu R (w createAll).

Po co jest:
- usuwa powtarzalny kod mapowania list,
- przenosi konwersję z serwisów do dedykowanych klas,
- ułatwia modyfikację odpowiedzi API w jednym miejscu.

---

### Konkretne fabryki DTO

#### CarResponseFactory

Rola:
- Konwertuje Car -> CarResponse.

Co zwraca:
- CarResponse.

Jak działa:
- create(entity) deleguje do CarResponse.fromEntity(entity).

Gdzie używana:
- CarService (mapowanie samochodu do odpowiedzi),
- UserService (mapowanie ulubionych samochodów użytkownika).

---

#### RentalResponseFactory

Rola:
- Konwertuje Rental -> RentalResponse.

Co zwraca:
- RentalResponse.

Jak działa:
- create(entity) deleguje do RentalResponse.fromEntity(entity).

Gdzie używana:
- RentalService:
  - createRental,
  - getUserRentals,
  - getAllRentals,
  - getOccupiedDates.

---

#### UserResponseFactory

Rola:
- Konwertuje User -> UserResponse.

Co zwraca:
- UserResponse.

Jak działa:
- create(entity) deleguje do UserResponse.fromEntity(entity).

Gdzie używana:
- UserService (getAllUsers).

---

## 2. Fabryki sortowania samochodów

### Klasa bazowa

- CarSortFactory

Rola:
- Tworzy obiekt Comparator<CarResponse> zależnie od strategii sortowania.

Najważniejsze metody:
- createComparator(): metoda fabryczna implementowana w podklasach.
- forStrategy(String sortBy): wybiera konkretną fabrykę.

Co zwraca:
- Comparator<CarResponse>.

Po co jest:
- usuwa switch z logiką sortowania z CarService,
- pozwala łatwo dodać nowe sortowanie przez nową klasę,
- rozdziela logikę biznesową pobierania danych od logiki technicznej sortowania.

---

### Konkretne fabryki sortowania

#### PriceAscSortFactory

Rola:
- Tworzy komparator sortujący po cenie rosnąco.

Co zwraca:
- Comparator<CarResponse> (pricePerDay rosnąco).

---

#### PriceDescSortFactory

Rola:
- Tworzy komparator sortujący po cenie malejąco.

Co zwraca:
- Comparator<CarResponse> (pricePerDay malejąco).

---

#### DefaultSortFactory

Rola:
- Tworzy komparator domyślny, gdy brak/nieznana strategia.

Co zwraca:
- Comparator<CarResponse> (porządek po id).

---

Gdzie cały zestaw jest używany:
- CarService.getAllCars(sortBy):
  - CarSortFactory.forStrategy(sortBy).createComparator(),
  - następnie cars.sort(...).

---

## 3. Fabryki akcji statusu wypożyczenia

### Produkt akcji

- RentalStatusAction

Rola:
- Interfejs opisujący operację execute(Rental rental).

Po co jest:
- ujednolica kontrakt dla różnych akcji na wypożyczeniu (zwrot, anulowanie).

---

### Klasa bazowa fabryki akcji

- RentalStatusActionFactory

Rola:
- Abstrakcyjna fabryka tworząca obiekt akcji na wypożyczeniu.

Najważniejsze metody:
- createAction(): metoda fabryczna implementowana przez podklasy.
- process(Rental rental): tworzy akcję i wykonuje ją.

Co zwraca:
- createAction() zwraca RentalStatusAction.
- process(...) nic nie zwraca (void), ale modyfikuje stan Rental.

Po co jest:
- przenosi szczegóły zmian statusu poza RentalService,
- porządkuje logikę i przygotowuje miejsce pod kolejne akcje w przyszłości.

---

### Konkretne fabryki akcji

#### ReturnRentalActionFactory

Rola:
- Tworzy akcję zwrotu wypożyczenia (wewnętrzna klasa ReturnRentalAction).

Co robi akcja po execute(rental):
- ustawia endDate na dzisiaj,
- przelicza realny koszt wypożyczenia,
- ustawia status rental na COMPLETED,
- ustawia status samochodu na AVAILABLE.

Co zwraca fabryka:
- RentalStatusAction (konkretnie ReturnRentalAction).

Gdzie używana:
- RentalService.returnRental(...).

---

#### CancelRentalActionFactory

Rola:
- Tworzy akcję anulowania wypożyczenia (wewnętrzna klasa CancelRentalAction).

Co robi akcja po execute(rental):
- ustawia status rental na CANCELLED,
- ustawia koszt na 0.

Co zwraca fabryka:
- RentalStatusAction (konkretnie CancelRentalAction).

Gdzie używana:
- RentalService.cancelRental(...).

---

## 4. Szybkie podsumowanie (co, gdzie, co zwraca)

1. ResponseFactory + CarResponseFactory/RentalResponseFactory/UserResponseFactory
- Co zwracają: DTO (CarResponse, RentalResponse, UserResponse) lub listy DTO.
- Gdzie: CarService, RentalService, UserService.
- Cel: centralizacja mapowania encja -> DTO.

2. CarSortFactory + PriceAsc/PriceDesc/Default
- Co zwracają: Comparator<CarResponse>.
- Gdzie: CarService.getAllCars.
- Cel: modularne sortowanie bez rozbudowy serwisu.

3. RentalStatusActionFactory + Return/Cancel
- Co zwracają: RentalStatusAction.
- Gdzie: RentalService.returnRental i cancelRental.
- Cel: wydzielenie logiki zmian statusu i kosztu wypożyczenia.

---

## 5. Dlaczego te fabryki mają sens akurat tutaj

- Projekt ma już rozdzielone warstwy (controller, service, dto, entity), więc factory naturalnie domykają separację odpowiedzialności.
- Serwisy są prostsze: mniej kodu technicznego, więcej czytelnej logiki biznesowej.
- Każdą rodzinę fabryk da się rozwijać bez przepisywania istniejących metod serwisowych.
- W połączeniu z Builder i Prototype wzorce nie kolidują:
  - Builder tworzy encje,
  - Prototype klonuje encje,
  - Factory tworzy DTO, komparatory i akcje domenowe.
