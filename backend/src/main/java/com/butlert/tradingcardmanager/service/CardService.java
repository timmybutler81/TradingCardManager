/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardService.java
 * This interface defines the behaviors of the service and what the service implementation must do. It is useful
 * to have this class when swapping out implementations.
 */
package com.butlert.tradingcardmanager.service;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CardService {
    Optional<CardDTO> addCard(CardDTO cardDTO);

    boolean deleteCard(int cardId);

    List<Card> getAllCards();

    Card findByCardId(int cardId);

    Map<String, Object> calculateCollectionStatistics();

    Map<String, BigDecimal> calculateCollectionValues();

    List<Card> addAllCardsFromFile(MultipartFile file);

    Optional<Card> updateCard(int cardNumber, CardDTO cardDTO);
}

