# TradingCardManager Frontend (Angular)

This is the Angular-based frontend for the TradingCardManager app.

## Features

- View trading cards
- Add, update, and delete cards
- Import cards from a `.txt` file
- View collection statistics and value

## Setup & Run

1. Open a terminal and navigate to the `frontend` folder
2. Install dependencies:

```bash
npm install
```

Run the development server:

```bash
ng serve
```

Visit http://localhost:4200

Notes

- This project uses standalone components (Angular 17+)
- A proxy configuration is included to route API calls to the backend
- No environment file setup is needed — it works automatically with the backend
