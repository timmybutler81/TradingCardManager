# Trading Card Manager

This project is a full-stack Trading Card Management application built with Angular (frontend) and Spring Boot (backend).

## Project Structure

/TradingCardManager
│
├── frontend/ → Angular application (user interface)
├── backend/ → Spring Boot application (REST API)
└── README.md → This file

## Prerequisites

- **Java 17+**
- **Node.js + npm** (https://nodejs.org)
- **Angular CLI** (`npm install -g @angular/cli`)
- **IntelliJ IDEA** (for backend)

---

## How to Run the Application

### 1. Start the Backend

- Open the `/backend` folder in IntelliJ
- Ensure JDK is set (e.g. Java 17+)
- Run the main class:

com.butlert.tradingcardmanager.TradingCardManagerApplication

The backend runs on `http://localhost:8080`.

---

### 2. Start the Frontend

In a terminal:

```bash
cd frontend
npm install
ng serve
```

The Angular app will run at http://localhost:4200
