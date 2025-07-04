### 🖥️ Backend `backend/README.md`

# TradingCardManager Backend (Spring Boot)

This is the Java/Spring Boot backend for the TradingCardManager app.

## Features

- RESTful API for managing trading cards
- Input validation with detailed error messages
- File import support
- Automatic calculation of collection statistics and value
- CORS configured to allow frontend access

## How to Run

1. Open the `/backend` folder in IntelliJ
2. Ensure your Java SDK is set (Java 17+)
3. Locate and run the `TradingCardManagerApplication.java` file

The backend will start on [http://localhost:8080](http://localhost:8080)

## API Endpoints

All endpoints are prefixed with `/api/cards`:

- `GET /api/cards` - List all cards
- `POST /api/cards` - Add a new card
- `PUT /api/cards/put/{cardNumber}` - Update a card
- `DELETE /api/cards/delete/{cardNumber}` - Delete a card
- `GET /api/cards/stats` - View statistics
- `GET /api/cards/values` - View collection value
- `POST /api/cards/import` - Import from a `.txt` file

## Notes

- CORS is preconfigured to allow requests from `http://localhost:4200`
- All errors are returned in JSON format
