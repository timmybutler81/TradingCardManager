package com.butlert.tradingcardmanager.mapper;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import com.butlert.tradingcardmanager.model.CardRarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Utility class for mapping between {@link Card} entities and {@link CardDTO} data transfer objects.
 * <p>
 * Provides static methods to convert domain entities to DTOs and vice versa,
 * applying validation and format transformations during the process.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 13, 2025</p>
 */
public class CardMapper {
    /**
     * Logger instance for recording application events and errors in the
     * {@link com.butlert.tradingcardmanager.controller.DatabaseConnectionController}.
     * <p>
     * Uses SLF4J's {@link org.slf4j.LoggerFactory} for standardized logging.
     * Helps in debugging and tracing database connection handling.
     */
    private static final Logger logger = LoggerFactory.getLogger(CardMapper.class);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CardMapper() {
        throw new UnsupportedOperationException("CardMapper is a utility class and cannot be instantiated.");
    }

    /**
     * Converts a {@link CardDTO} into a {@link Card} entity.
     * <p>
     * Performs field mapping, enum parsing for rarity, and string-to-date/decimal conversions.
     * Throws {@link IllegalArgumentException} if any values are invalid or improperly formatted.
     * </p>
     *
     * @param cardDTO the DTO containing card data to convert
     * @return the corresponding {@link Card} entity
     * @throws IllegalArgumentException if the input data is invalid
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
     * Converts a {@link Card} entity into a {@link CardDTO} object.
     * <p>
     * Transforms all fields, including dates and BigDecimal, into string representations suitable for the frontend.
     * </p>
     *
     * @param card the {@link Card} entity to convert
     * @return the corresponding {@link CardDTO}
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
