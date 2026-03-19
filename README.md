# 🏢 Room Reservation System

System rezerwacji pokojów zbudowany w Java 21 + Spring Boot 3.

## 🚀 Technologie

- **Java 21** + **Spring Boot 3.5**
- **PostgreSQL** — baza danych
- **Spring Data JPA** — warstwa persistence
- **Docker** + **docker-compose** — konteneryzacja
- **GitHub Actions** — CI/CD pipeline
- **JUnit 5** + **Mockito** — testy jednostkowe

## 🏗 Architektura

Projekt zbudowany zgodnie z zasadami **DDD** (Domain-Driven Design):
```
src/main/java/
├── domain/          ← logika biznesowa (czysta Java)
│   ├── room/        ← Room (Aggregate Root), RoomRepository
│   └── reservation/ ← Reservation, TimeSlot (Value Object)
├── application/     ← ReservationService, RoomMapper
└── interfaces/      ← REST API (RoomController)
```

## ✨ Funkcje

- ✅ Tworzenie pokojów
- ✅ Rezerwowanie pokojów z walidacją konfliktów terminów
- ✅ Anulowanie rezerwacji (reguła 24h)
- ✅ Przeglądanie pokojów i rezerwacji
- ✅ Prosty frontend HTML

## 🐳 Uruchomienie przez Docker
```bash
docker-compose up --build -d
```

Aplikacja dostępna na: `http://localhost:8080`

## 🧪 Testy
```bash
mvn clean verify
```

## 📡 API Endpoints

| Metoda | URL | Opis |
|--------|-----|------|
| POST | `/api/rooms` | Dodaj pokój |
| GET | `/api/rooms` | Lista pokojów |
| POST | `/api/rooms/{id}/reservations` | Zarezerwuj pokój |
| GET | `/api/rooms/reservations` | Lista rezerwacji |
| DELETE | `/api/rooms/reservations/{id}` | Anuluj rezerwację |