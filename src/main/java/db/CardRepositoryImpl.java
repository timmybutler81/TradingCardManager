/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardRepositoryImpl.java
 * This class is the current implementation of the card repository. It simulates all crud operations that would
 * normally be handled by a database.
 */
package main.java.db;

import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {
    private List<Card> cardList = new ArrayList<>();

    /**
     * method: saveCard
     * parameters: card
     * return: boolean
     * purpose: Simulates adding a card to database and ensures no duplicates by ID
     */
    @Override
    public boolean saveCard(Card card) {
        if (findByCardId(card.getCardNumber()) != null) {
            System.out.println("Card with ID " + card.getCardNumber() + " already exists.");
            return false;
        }
        cardList.add(card);
        return true;
    }

    /**
     * method: deleteCard
     * parameters: card
     * return: boolean
     * purpose: Simulates deleting a card to database
     */
    @Override
    public boolean deleteCard(int cardId) {
        Card card = findByCardId(cardId);
        if (card != null) {
            cardList.remove(card);
            return true;
        }
        return false;
    }

    /**
     * method: findAllCards
     * parameters: none
     * return: List of cards
     * purpose: Simulates retrieving all cards from the database
     */
    @Override
    public List<Card> findAllCards() {
        return List.copyOf(cardList);
    }

    /**
     * method: findByCardId
     * parameters: cardId
     * return: Card
     * purpose: Simulates finding a card from the database
     */
    @Override
    public Card findByCardId(int cardId) {
        return cardList.stream()
                .filter(card -> card.getCardNumber() == cardId)
                .findFirst()
                .orElse(null);
    }
}
