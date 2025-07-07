/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardServiceImpl.java
 * This is the current implementation of the service logic. This contains the majority of the business logic that
 * should be meeting the user requirements.
 */
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

@Service
@Transactional
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardParser cardParser;
    private final ReadTextFile readTextFile;
    private final CardValidator cardValidator;
    private final HandlerMapping resourceHandlerMapping;

    public CardServiceImpl(CardRepository cardRepository,
                           CardParser cardParser,
                           ReadTextFile readTextFile,
                           CardValidator cardValidator, CardValidator cardValidator1, @Qualifier("resourceHandlerMapping") HandlerMapping resourceHandlerMapping) {
        this.cardRepository = cardRepository;
        this.cardParser = cardParser;
        this.readTextFile = readTextFile;
        this.cardValidator = cardValidator;
        this.resourceHandlerMapping = resourceHandlerMapping;
    }

    /**
     * method: addCard
     * parameters: card
     * return: Optional<Card>
     * purpose: Adds card to repository if it doesn't already exist by cardNumber
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
     * method: deleteCard
     * parameters: cardId (cardNumber)
     * return: boolean
     * purpose: Deletes card by cardNumber and returns whether deletion occurred
     */
    @Override
    public boolean deleteCard(int cardId) {
        return cardRepository.deleteByCardNumber(cardId) > 0;
    }

    /**
     * method: updateCard
     * parameters: cardNumber, Card
     * return: Card
     * purpose: Finds and updates a card in the repository
     */

    @Override
    public Optional<Card> updateCard(int cardNumber, CardDTO cardDTO) {
        Card updatedCard;

        try {
            updatedCard = CardMapper.toEntity(cardDTO); // Convert DTO to entity
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

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    /**
     * method: findByCardId
     * parameters: cardId (cardNumber)
     * return: Card
     * purpose: Returns card matching given cardNumber or null if not found
     */
    @Override
    public Card findByCardId(int cardId) {
        return cardRepository.findByCardNumber(cardId).orElse(null);
    }

    /**
     * method: calculateCollectionStatistics
     * parameters: none
     * return: Map of Stat and value
     * purpose: Calculates statistics about cards in the repository
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
     * method: calculateCollectionValues
     * parameters: none
     * return: Map of market/owner and the value
     * purpose: Calculates the value of the entire collection based on market increase and decrease
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
     * method: addAllCardsFromFile
     * parameters: filePath
     * return: List of Cards
     * purpose: Processes the read lines from util classes and calls the repository to insert them
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
                    System.out.println("Reason: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }

        return importedCards;
    }

}
