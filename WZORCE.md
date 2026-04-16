# Wzorce projektowe – SwiftRent

---

## Command (Polecenie)

**Idea:** Enkapsuluje żądanie jako obiekt. Dzięki temu można kolejkować operacje, logować je lub cofać bez znajomości szczegółów wykonania.

### Użycie 1 – Rezerwacja pojazdu
- **Interfejs:** `RentalBookingCommand`
- **Implementacja:** `RentalBookingCommandImpl`
- Polecenie przyjmuje tablicę rejestracyjną i e-mail użytkownika, a przy wywołaniu `execute()` rejestruje rezerwację.

### Użycie 2 – Zmiana statusu pojazdu
- **Interfejs:** `CarStatusUpdateCommand`
- **Implementacja:** `CarStatusUpdateCommandImpl`
- Polecenie przyjmuje tablicę i nowy status (np. AVAILABLE / UNAVAILABLE) i aktualizuje go przy `execute()`.

### Użycie 3 – Zmiana e-maila użytkownika
- **Interfejs:** `UserEmailChangeCommand`
- **Implementacja:** `UserEmailChangeCommandImpl`
- Polecenie przyjmuje stary i nowy adres e-mail i wykonuje zmianę przy `execute()`.

---

## Interpreter

**Idea:** Definiuje gramatykę dla prostego języka i dostarcza interpreter, który przetwarza zdania w tej gramatyce. Przydatny do budowania filtrów i reguł biznesowych.

### Użycie 1 – Status wypożyczenia
- **Interfejs:** `RentalStatusExpression`
- **Implementacja:** `RentalStatusExpressionImpl`
- Sprawdza, czy podany status wypożyczenia (np. „ACTIVE") zgadza się z oczekiwanym.

### Użycie 2 – Zakres dat
- **Interfejs:** `DateRangeExpression`
- **Implementacja:** `DateRangeExpressionImpl`
- Weryfikuje, czy data wypożyczenia mieści się w zadanym przedziale `[from, to]`.

### Użycie 3 – Rola użytkownika
- **Interfejs:** `UserRoleExpression`
- **Implementacja:** `UserRoleExpressionImpl`
- Sprawdza, czy rola użytkownika (np. „ADMIN") odpowiada wymaganej roli.

---

## Iterator

**Idea:** Udostępnia sposób sekwencyjnego przeglądania elementów kolekcji bez ujawniania jej wewnętrznej struktury.

### Użycie 1 – Katalog dostępnych aut
- **Interfejs:** `AvailableCarCatalogIterator`
- **Implementacja:** `AvailableCarCatalogIteratorImpl`
- Iteruje po liście dostępnych pojazdów od pierwszego do ostatniego.

### Użycie 2 – Historia wypożyczeń użytkownika
- **Interfejs:** `UserRentalHistoryIterator`
- **Implementacja:** `UserRentalHistoryIteratorImpl`
- Iteruje po historii wypożyczeń **od najnowszego** wpisu (odwrócona kolejność).

### Użycie 3 – Kolejka oczekujących zatwierdzeń
- **Interfejs:** `PendingApprovalIterator`
- **Implementacja:** `PendingApprovalIteratorImpl`
- Iteruje po kolejce FIFO wniosków czekających na zatwierdzenie przez administratora.

---

## Mediator

**Idea:** Wprowadza centralny obiekt (mediatora), który pośredniczy w komunikacji między komponentami. Komponenty nie rozmawiają ze sobą bezpośrednio, co zmniejsza zależności.

### Użycie 1 – Proces rezerwacji
- **Interfejs:** `RentalBookingMediator`
- **Implementacja:** `RentalBookingMediatorImpl`
- Koordynuje komunikację między sprawdzaniem dostępności auta a potwierdzeniem płatności. Gdy oba sygnały nadejdą, finalizuje rezerwację.

### Użycie 2 – Zwrot pojazdu
- **Interfejs:** `CarReturnMediator`
- **Implementacja:** `CarReturnMediatorImpl`
- Koordynuje wynik inspekcji ze zmianą statusu auta. Jeśli inspekcja przeszła pomyślnie, auto wraca do puli dostępnych; jeśli wykryto uszkodzenie, trafia do serwisu.

### Użycie 3 – Rejestracja użytkownika
- **Interfejs:** `UserRegistrationMediator`
- **Implementacja:** `UserRegistrationMediatorImpl`
- Koordynuje walidację danych z tworzeniem konta. Po pomyślnej walidacji tworzy konto, po jego założeniu wysyła e-mail powitalny.

---

## Memento (Pamiątka)

**Idea:** Pozwala zapisać i przywrócić poprzedni stan obiektu bez ujawniania szczegółów jego implementacji. Wzorzec składa się z trzech ról: **Originator** (tworzy/przywraca pamiątkę), **Memento** (przechowuje stan), **Caretaker** (zarządza historią pamiątek).

### Użycie 1 – Stan wypożyczenia
- **Interfejs:** `RentalStateMemento` | **Implementacja:** `RentalStateMementoImpl`
- **Originator:** `RentalStateOriginator` | **Caretaker:** `RentalStateCaretaker`
- Zapisuje migawkę statusu, tablicy rejestracyjnej i e-maila użytkownika. Umożliwia cofnięcie zmiany statusu wypożyczenia.

### Użycie 2 – Konfiguracja pojazdu
- **Interfejs:** `CarConfigMemento` | **Implementacja:** `CarConfigMementoImpl`
- **Originator:** `CarConfigOriginator` | **Caretaker:** `CarConfigCaretaker`
- Zapisuje migawkę modelu, statusu i stawki dziennej. Umożliwia cofnięcie zmiany ceny lub statusu auta.

### Użycie 3 – Preferencje użytkownika
- **Interfejs:** `UserPreferencesMemento` | **Implementacja:** `UserPreferencesMementoImpl`
- **Originator:** `UserPreferencesOriginator` | **Caretaker:** `UserPreferencesCaretaker`
- Zapisuje migawkę języka, waluty i zgody na newsletter. Umożliwia przywrócenie poprzednich ustawień użytkownika.
