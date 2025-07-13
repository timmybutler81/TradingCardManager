CREATE TABLE IF NOT EXISTS card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number INT NOT NULL CHECK (card_number > 0),
    card_game VARCHAR(255) NOT NULL,
    card_name VARCHAR(255) NOT NULL,
    rarity VARCHAR(255),
    date_purchased DATE,
    date_set_published DATE,
    purchase_price DECIMAL(12,2) CHECK (purchase_price > 0),
    foiled BOOLEAN NOT NULL
);