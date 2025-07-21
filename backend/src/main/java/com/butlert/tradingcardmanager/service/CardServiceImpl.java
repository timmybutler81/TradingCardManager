package com.butlert.tradingcardmanager.service;

import com.butlert.tradingcardmanager.mapper.CardMapper;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.repository.CardRepository;
import com.butlert.tradingcardmanager.utils.CardDateUtil;
import com.butlert.tradingcardmanager.utils.CardParser;
import com.butlert.tradingcardmanager.utils.ReadTextFile;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import com.butlert.tradingcardmanager.utils.validation.ValidatorResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the {@link CardService} interface containing business logic
 * for managing cards in the Trading Card Manager application.
 * <p>
 * This service handles validation, mapping, statistics, and value calculations,
 * and acts as the main logic layer between the controller and the repository.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> June 18, 2025</p>
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {
    /**
     * Repository interface for performing CRUD operations on cards.
     */
    private final CardRepository cardRepository;

    /**
     * Utility for parsing text input into card DTOs.
     */
    private final CardParser cardParser;

    /**
     * Utility for reading raw text file content.
     */
    private final ReadTextFile readTextFile;

    /**
     * Validates card fields and structure before persistence.
     */
    private final CardValidator cardValidator;

    /**
     * Spring MVC handler mapping, injected by qualifier.
     * Used for file resource resolution (not directly used in this class).
     */
    private final HandlerMapping resourceHandlerMapping;

    /**
     * Constructs a CardServiceImpl with all required dependencies for card operations.
     *
     * @param cardRepository           the repository used to access card data
     * @param cardParser               utility for parsing cards from raw input
     * @param readTextFile             utility for reading text file contents
     * @param cardValidator            validates card data before persistence
     * @param resourceHandlerMapping   Spring MVC handler mapping, injected by qualifier
     */
    public CardServiceImpl(CardRepository cardRepository,
                           CardParser cardParser,
                           ReadTextFile readTextFile,
                           CardValidator cardValidator, @Qualifier("resourceHandlerMapping") HandlerMapping resourceHandlerMapping) {
        this.cardRepository = cardRepository;
        this.cardParser = cardParser;
        this.readTextFile = readTextFile;
        this.cardValidator = cardValidator;
        this.resourceHandlerMapping = resourceHandlerMapping;
    }

    /**
     * Adds a new card to the repository if it does not already exist.
     * Validates the card before saving.
     *
     * @param cardDTO the card data to add
     * @return an {@link Optional} containing the created {@link CardDTO}, or empty if the card already exists
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public Optional<CardDTO> addCard(CardDTO cardDTO) {
        Card card = CardMapper.toEntity(cardDTO);
        ValidatorResult result = cardValidator.validateCard(card);

        if (!result.isValid()) {
            throw new IllegalArgumentException("Validation failed: " + result.getMessage());
        }

        if (cardRepository.existsByCardNumber(cardDTO.getCardNumber())) {
            return Optional.empty();
        }

        Card saved = cardRepository.save(card);
        return Optional.of(CardMapper.toDto(saved));
    }

    /**
     * Deletes a card by its card number.
     *
     * @param cardId the card number of the card to delete
     * @return true if the card was deleted, false if it did not exist
     */
    @Override
    public boolean deleteCard(int cardId) {
        return cardRepository.deleteByCardNumber(cardId) > 0;
    }

    /**
     * Updates an existing card with new values from the provided DTO.
     *
     * @param cardNumber the number of the card to update
     * @param cardDTO    the updated card data
     * @return an {@link Optional} containing the updated {@link Card}, or empty if not found
     * @throws IllegalArgumentException if validation fails or input is invalid
     */
    @Override
    public Optional<Card> updateCard(int cardNumber, CardDTO cardDTO) {
        Card updatedCard;

        try {
            updatedCard = CardMapper.toEntity(cardDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid data: " + e.getMessage());
        }

        ValidatorResult result = cardValidator.validateCard(updatedCard);

        if (!result.isValid()) {
            throw new IllegalArgumentException("Validation failed: " + result.getMessage());
        }

        Optional<Card> existingCard = cardRepository.findByCardNumber(cardNumber);
        if (existingCard.isEmpty()) {
            return Optional.empty();
        }

        Card cardToUpdate = existingCard.get();

        cardToUpdate.setCardGame(updatedCard.getCardGame());
        cardToUpdate.setCardName(updatedCard.getCardName());
        cardToUpdate.setRarity(updatedCard.getRarity());
        cardToUpdate.setDatePurchased(updatedCard.getDatePurchased());
        cardToUpdate.setDateSetPublished(updatedCard.getDateSetPublished());
        cardToUpdate.setPurchasePrice(updatedCard.getPurchasePrice());
        cardToUpdate.setFoiled(updatedCard.isFoiled());

        return Optional.of(cardRepository.save(cardToUpdate));
    }

    /**
     * Retrieves all cards stored in the repository.
     *
     * @return a list of all {@link Card} entities
     */
    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    /**
     * Finds a card by its card number.
     *
     * @param cardId the card number to search
     * @return the matching {@link Card}, or null if not found
     */
    @Override
    public Card findByCardId(int cardId) {
        return cardRepository.findByCardNumber(cardId).orElse(null);
    }

    /**
     * Calculates statistics on the card collection, including:
     * - Total spent
     * - Total cards
     * - Foiled card count
     * - Percentage of foiled cards
     *
     * @return a map containing statistical keys and their corresponding values
     */
    @Override
    public Map<String, Object> calculateCollectionStatistics() {
        List<Card> cards = cardRepository.findAll();
        BigDecimal totalSpend = BigDecimal.ZERO;
        int foilCount = 0;
        int totalCards = cards.size();

        for (Card card : cards) {
            if (card.isFoiled()) {
                foilCount++;
            }
            if (card.getPurchasePrice() != null) {
                totalSpend = totalSpend.add(card.getPurchasePrice());
            }
        }

        double percentFoiled = cards.isEmpty() ? 0.0 : (foilCount * 100.0) / totalCards;

        return Map.of(
                "totalSpent", totalSpend,
                "totalCards", totalCards,
                "totalFoiled", foilCount,
                "percentFoiled", percentFoiled
        );
    }

    /**
     * Calculates the total market and owner value of all cards in the collection.
     * Value adjustments are based on rarity and time since purchase/publication.
     *
     * @return a map containing "marketValue" and "ownerValue" totals
     */
    @Override
    public Map<String, BigDecimal> calculateCollectionValues() {
        List<Card> cards = cardRepository.findAll();
        CardDateUtil cardDateUtil = new CardDateUtil();

        BigDecimal totalMarketValue = BigDecimal.ZERO;
        BigDecimal totalOwnerValue = BigDecimal.ZERO;

        for (Card card : cards) {
            BigDecimal basePrice = card.getPurchasePrice();
            if (basePrice == null) continue;

            CardRarity rarity = card.getRarity();

            long marketIntervals = cardDateUtil.calculateDayInterval(card.getDateSetPublished());
            long ownerIntervals = cardDateUtil.calculateDayInterval(card.getDatePurchased());

            double marketRate = rarity.getMarketRate();
            double ownerRate = rarity.getOwnerRate();

            BigDecimal marketMultiplier = BigDecimal.valueOf(Math.pow(1 + marketRate, marketIntervals));
            BigDecimal ownerMultiplier = BigDecimal.valueOf(Math.pow(1 + ownerRate, ownerIntervals));

            BigDecimal marketValue = basePrice.multiply(marketMultiplier);
            BigDecimal ownerValue = basePrice.multiply(ownerMultiplier);

            if (rarity == CardRarity.COMMON) {
                BigDecimal floor = new BigDecimal("0.20");
                if (marketValue.compareTo(floor) < 0) marketValue = floor;
                if (ownerValue.compareTo(floor) < 0) ownerValue = floor;
            }

            totalMarketValue = totalMarketValue.add(marketValue);
            totalOwnerValue = totalOwnerValue.add(ownerValue);

        }

        return Map.of(
                "marketValue", totalMarketValue,
                "ownerValue", totalOwnerValue
        );
    }

    /**
     * Imports multiple cards from a provided text file.
     * Each line is parsed and validated; valid cards are saved and added to the system.
     *
     * @param file the uploaded file containing card data
     * @return a list of successfully imported {@link Card} entities
     * @throws RuntimeException if the file cannot be read
     */
    @Override
    public List<Card> addAllCardsFromFile(MultipartFile file) {
        List<Card> importedCards = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Optional<CardDTO> tempDto = cardParser.parseLine(line);

                    if (tempDto.isPresent()) {
                        Optional<CardDTO> savedDto = addCard(tempDto.get());
                        savedDto
                                .map(CardMapper::toEntity)
                                .ifPresent(importedCards::add);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid line: " + line);
                    System.out.println("Reason: " + e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }

        return importedCards;
    }
}
