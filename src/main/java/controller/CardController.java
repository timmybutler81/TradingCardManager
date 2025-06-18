package main.java.controller;

import main.java.db.Card;
import main.java.service.CardService;
import main.java.service.CardServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CardController {
    private CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    public boolean addCard(Card card) {
        return cardService.addCard(card);
    }

    public void deleteCard(int cardId) {
        cardService.deleteCard(cardId);
    }

    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    public Card getCardById(int cardId) {
        return cardService.findByCardId(cardId);
    }

    public Map<String, Object> calculateCollectionStatistics() {
        return cardService.calculateCollectionStatistics();
    }

    public Map<String, BigDecimal> calculateCollectionValue() {
        return cardService.calculateCollectionValues();
    }

    public List<Card> importCardsFromFile(String filePath) {
        return cardService.addAllCardsFromFile(filePath);
    }
}
