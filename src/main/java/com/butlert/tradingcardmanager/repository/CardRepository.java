/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardRepository.java
 * This interface is used to define the functionality the responsibility must implement. It is used so
 * repository implementations can be swapped out easily (i.e. the database part of this project)
 */
package com.butlert.tradingcardmanager.repository;

import com.butlert.tradingcardmanager.model.Card;

import java.util.List;

public interface CardRepository {
    boolean saveCard(Card card);

    boolean deleteCard(int cardId);

    List<Card> findAllCards();

    Card findByCardId(int cardId);
}
