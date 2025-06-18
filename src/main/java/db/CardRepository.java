/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardRepository.java
 * This interface is used to define the functionality the responsibility must implement. It is used so
 * repository implementations can be swapped out easily (i.e. the database part of this project)
 */
package main.java.db;

import java.util.List;

public interface CardRepository {
    public boolean saveCard(Card card);
    public boolean deleteCard(int cardId);
    public List<Card> findAllCards();
    public Card findByCardId(int cardId);
}
