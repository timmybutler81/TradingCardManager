/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardService.java
 * This interface defines the behaviors of the service and what the service implementation must do. It is useful
 * to have this class when swapping out implementations.
 */
package main.java.service;

import main.java.db.Card;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CardService {
    public boolean addCard(Card card);
    public boolean deleteCard(int cardId);
    public List<Card> getAllCards();
    public Card findByCardId(int cardId);
    public Map<String, Object> calculateCollectionStatistics();
    public Map<String, BigDecimal> calculateCollectionValues();
    public List<Card> addAllCardsFromFile(String filePath);

}
