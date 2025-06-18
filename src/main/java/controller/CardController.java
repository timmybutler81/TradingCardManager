/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardController.java
 * This class is the main access point for the GUI and uses this exclusively to call the back end part of the
 * application. It has methods for all functionality of the app.
 */
package main.java.controller;

import main.java.db.Card;
import main.java.service.CardService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CardController {
    private CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * method: addCard
     * parameters: card
     * return: boolean
     * purpose: access point to add card from GUI
     */
    public boolean addCard(Card card) {
        return cardService.addCard(card);
    }

    /**
     * method: deleteCard
     * parameters: card
     * return: boolean
     * purpose: access point to delete card from GUI
     */
    public void deleteCard(int cardId) {
        cardService.deleteCard(cardId);
    }

    /**
     * method: getAllCards
     * parameters: none
     * return: List of Cards
     * purpose: access point to list all cards from GUI
     */
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    /**
     * method: getCardById
     * parameters: cardId
     * return: Card
     * purpose: access point to look up a card by ID from GUI
     */
    public Card getCardById(int cardId) {
        return cardService.findByCardId(cardId);
    }

    /**
     * method: calculateCollectionStatistics
     * parameters: none
     * return: Map of Stats and values
     * purpose: access point to retrieve card statistics for the GUI
     */
    public Map<String, Object> calculateCollectionStatistics() {
        return cardService.calculateCollectionStatistics();
    }

    /**
     * method: calculateCollectionValue
     * parameters: none
     * return: Map of owner/market and values
     * purpose: access point to retrieve collection value for the GUI
     */
    public Map<String, BigDecimal> calculateCollectionValue() {
        return cardService.calculateCollectionValues();
    }

    /**
     * method: importCardsFromFile
     * parameters: filePath
     * return: List of cards
     * purpose: access point to import cards from file for the GUI
     */
    public List<Card> importCardsFromFile(String filePath) {
        return cardService.addAllCardsFromFile(filePath);
    }
}
