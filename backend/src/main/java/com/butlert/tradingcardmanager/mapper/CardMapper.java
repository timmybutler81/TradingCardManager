package com.butlert.tradingcardmanager.mapper;

import com.butlert.tradingcardmanager.controller.DatabaseConnectionController;
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
        logger.info("Card created from mapper {}", card.toString());
        return card;
    }


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
