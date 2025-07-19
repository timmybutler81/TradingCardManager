package com.butlert.tradingcardmanager.service;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface defining the contract for all card-related business operations.
 * <p>
 * This layer acts as an intermediary between the controller and the repository,
 * encapsulating business logic such as validation, transformation, and calculations.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> June 18, 2025</p>
 */
public interface CardService {
    /**
     * Adds a new card to the system based on the provided DTO.
     *
     * @param cardDTO the card data to add
     * @return an {@link Optional} containing the created CardDTO, or empty if the card already exists
     */
    Optional<CardDTO> addCard(CardDTO cardDTO);

    /**
     * Deletes a card by its card number.
     *
     * @param cardId the card number to delete
     * @return true if the card was successfully deleted, false otherwise
     */
    boolean deleteCard(int cardId);

    /**
     * Retrieves all cards from the system.
     *
     * @return a list of all stored {@link Card} entities
     */
    List<Card> getAllCards();

    /**
     * Retrieves a card by its card number.
     *
     * @param cardId the card number to find
     * @return the corresponding {@link Card} if found
     */
    Card findByCardId(int cardId);

    /**
     * Calculates statistics across the entire card collection.
     *
     * @return a map of statistical keys and their corresponding values
     */
    Map<String, Object> calculateCollectionStatistics();

    /**
     * Calculates both owner and market values for the card collection.
     *
     * @return a map with "owner" and "market" as keys and their respective values
     */
    Map<String, BigDecimal> calculateCollectionValues();

    /**
     * Imports multiple cards from a file and adds them to the system.
     *
     * @param file the uploaded text file containing card data
     * @return a list of successfully imported {@link Card} entities
     */
    List<Card> addAllCardsFromFile(MultipartFile file);

    /**
     * Updates an existing card using the given card number and updated DTO data.
     *
     * @param cardNumber the card number of the card to update
     * @param cardDTO    the updated card data
     * @return an {@link Optional} containing the updated {@link Card}, or empty if not found
     */
    Optional<Card> updateCard(int cardNumber, CardDTO cardDTO);
}

