# Hotel Reservation Backend

Backend developed with Spring Boot for a hotel reservation system.

## Features

- Room availability search by date and type
- Reservation creation
- Additional services management
- Check-in with digital key generation
- Check-out with final invoice generation
- In-memory state management using Java collections
- Facade pattern implementation

## Technologies

- Java 21
- Spring Boot
- Maven
- Lombok
- Bean Validation

## Main Endpoints

- `POST /api/hotel/reservar`
- `GET /api/hotel/disponibilidad`
- `POST /api/hotel/servicios/{reservaId}`
- `PUT /api/hotel/checkin/{reservaId}`
- `PUT /api/hotel/checkout/{reservaId}`
- `GET /api/hotel/reserva/{reservaId}`
- `GET /api/health`

## Run locally

```bash
./mvnw spring-boot:run