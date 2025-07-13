/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 13, 2025
 * CardMapper.java
 * This class is used for converting entities to DTOs and vice versa.
 */
package com.butlert.tradingcardmanager.mapper;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import com.butlert.tradingcardmanager.model.CardRarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CardMapper {
    private static final Logger logger = LoggerFactory.getLogger(CardMapper.class);

    /**
     * method: toEntity
     * parameters: CardDTO
     * return: Card
     * purpose: converts CardDTO to Card entity
     */
    public static Card toEntity(CardDTO cardDTO) {
        Card card = new Card();

        card.setId(cardDTO.getId());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardGame(cardDTO.getCardGame());
        card.setCardName(cardDTO.getCardName());
        card.setFoiled(cardDTO.isFoiled());

        try {
            if (cardDTO.getRarity() == null || cardDTO.getRarity().isBlank()) {
                throw new IllegalArgumentException("Rarity is required.");
            }

            card.setRarity(CardRarity.valueOf(cardDTO.getRarity().toUpperCase()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Rarity: " + cardDTO.getRarity());
        }
        try {
            card.setDatePurchased(LocalDate.parse(cardDTO.getDatePurchased()));
            card.setDateSetPublished(LocalDate.parse(cardDTO.getDateSetPublished()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format (expected yyyy-MM-dd)");
        }
        try {
            card.setPurchasePrice(new BigDecimal(cardDTO.getPurchasePrice()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid purchase price format");
        }
        logger.info("Card created from mapper {}", card);
        return card;
    }

    /**
     * method: toDto
     * parameters: Card
     * return: CardDTO
     * purpose: converts Card to CardDTO object
     */
    public static CardDTO toDto(Card card) {
        return new CardDTO(
                card.getId(),
                card.getCardNumber(),
                card.getCardGame(),
                card.getCardName(),
                card.getRarity().name(),
                card.getDatePurchased().toString(),
                card.getDateSetPublished().toString(),
                card.getPurchasePrice().toPlainString(),
                card.isFoiled()
        );
    }
}
