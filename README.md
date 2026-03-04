# SwiftRent

Aplikacja do wypożyczania samochodów. Projekt składa się z backendu w Spring Boot i frontendu w Next.js.

## O projekcie

SwiftRent to system zarządzania wypożyczalnią samochodów. Użytkownicy mogą przeglądać dostępne auta, rezerwować je na wybrany termin i zarządzać swoimi rezerwacjami. Administratorzy mają dostęp do panelu, w którym mogą dodawać nowe samochody i kontrolować wszystkie wypożyczenia.

## Funkcjonalności

**Dla użytkowników:**
- Rejestracja i logowanie (JWT)
- Przeglądanie katalogu samochodów
- Rezerwacja auta na wybrany okres
- Podgląd swoich aktywnych i zakończonych wypożyczeń
- Dodawanie samochodów do ulubionych

**Dla administratorów:**
- Zarządzanie flotą samochodów (CRUD)
- Podgląd wszystkich wypożyczeń w systemie

## Stack technologiczny

### Backend
- Java 17
- Spring Boot 4.0
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL

### Frontend
- Next.js 14
- React 18
- TypeScript
- Tailwind CSS
- Axios

## Uruchomienie

### Backend

Wymagania: Java 17+, PostgreSQL

```bash
cd backend
./mvnw spring-boot:run
```

Backend startuje na `http://localhost:8080`

### Frontend

Wymagania: Node.js 18+

```bash
cd frontend
npm install
npm run dev
```

Frontend dostępny pod `http://localhost:3000`

## Konfiguracja

Przed uruchomieniem skonfiguruj połączenie z bazą danych w `backend/src/main/resources/application.properties`.

## Struktura projektu

```
SwiftRent/
├── backend/           # Spring Boot API
│   └── src/main/java/ma/swiftrent/
│       ├── config/    # Konfiguracja Security, CORS, etc.
│       ├── controller/# REST controllery
│       ├── dto/       # Data Transfer Objects
│       ├── entity/    # Encje JPA
│       ├── repository/# Repozytoria Spring Data
│       └── service/   # Logika biznesowa
├── frontend/          # Next.js App
│   └── src/
│       ├── app/       # Strony (App Router)
│       ├── components/# Komponenty React
│       ├── context/   # React Context (Auth)
│       └── lib/       # Konfiguracja API
└── uploads/           # Zdjęcia samochodów
```

## Domyślni użytkownicy

Aplikacja przy starcie tworzy przykładowe konta:
- Admin: `admin@swiftrent.pl` 
- User: `user@swiftrent.pl`


