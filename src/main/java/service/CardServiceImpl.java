/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardServiceImpl.java
 * This is the current implementation of the service logic. This contains the majority of the business logic that
 * should be meeting the user requirements.
 */
package main.java.service;

import main.java.db.Card;
import main.java.db.CardRepository;
import main.java.utils.CardDateUtil;
import main.java.utils.CardParser;
import main.java.utils.CardRarity;
import main.java.utils.FileReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    CardParser cardParser;
    FileReader fileReader;

    public CardServiceImpl(CardRepository repository, CardParser parser, FileReader reader) {
        this.cardRepository = repository;
        this.cardParser = parser;
        this.fileReader = reader;
    }

    /**
     * method: addCard
     * parameters: card
     * return: boolean
     * purpose: pass through to call repository to add card. Used for additional logic when needed
     */
    @Override
    public boolean addCard(Card card) {
        return cardRepository.saveCard(card);
    }

    /**
     * method: deleteCard
     * parameters: cardId
     * return: boolean
     * purpose: pass through to call repository to delete card. Used for additional logic when needed
     */
    @Override
    public boolean deleteCard(int cardId) {
        cardRepository.deleteCard(cardId);
        return false;
    }

    /**
     * method: getAllCards
     * parameters: none
     * return: List of Cards
     * purpose: pass through to call repository to get all the cards in the "database"
     */
    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAllCards();
    }

    /**
     * method: findByCardId
     * parameters: cardId
     * return: Card
     * purpose: pass through to find a card by ID in the "database"
     */
    @Override
    public Card findByCardId(int cardId) {
        return cardRepository.findByCardId(cardId);
    }

    /**
     * method: calculateCollectionStatistics
     * parameters: none
     * return: Map of Stat and value
     * purpose: calculated statistics about cards in the "database"
     */
    @Override
    public Map<String, Object> calculateCollectionStatistics() {
        List<Card> cards = cardRepository.findAllCards();
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
     * purpose: calculated the value of the entire collection based on market increase and decrease
     */
    @Override
    public Map<String, BigDecimal> calculateCollectionValues() {
        List<Card> cards = cardRepository.findAllCards();
        CardDateUtil cardDateUtil = new CardDateUtil();

        BigDecimal totalMarketValue = BigDecimal.ZERO;
        BigDecimal totalOwnerValue = BigDecimal.ZERO;

        for (Card card : cards) {
            BigDecimal basePrice = card.getPurchasePrice();
            if (basePrice == null) continue;

            CardRarity rarity = card.getRarity();

            // Calculate intervals
            long marketIntervals = cardDateUtil.calculateDayInterval(card.getDateSetPublished());
            long ownerIntervals = cardDateUtil.calculateDayInterval(card.getDatePurchased());

            // Compute multipliers
            double marketRate = rarity.getMarketRate();
            double ownerRate = rarity.getOwnerRate();

            BigDecimal marketMultiplier = BigDecimal.valueOf(Math.pow(1 + marketRate, marketIntervals));
            BigDecimal ownerMultiplier  = BigDecimal.valueOf(Math.pow(1 + ownerRate, ownerIntervals));

            BigDecimal marketValue = basePrice.multiply(marketMultiplier);
            BigDecimal ownerValue  = basePrice.multiply(ownerMultiplier);

            // Apply floor for COMMON rarity
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
    public List<Card> addAllCardsFromFile(String filePath) {
        List<Card> importedCards = new ArrayList<>();
        List<String> lines = fileReader.readLines(filePath);

        for (String line: lines) {
            try {
                Optional<Card> tempCard = cardParser.parseLine(line);
                tempCard.ifPresent(card -> {
                    if (cardRepository.saveCard(card)) {
                        importedCards.add(card);
                    } else {
                        System.out.println("Duplicate card not added: " + card.getCardNumber());
                    }
                });
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping invalid line: " + line);
                System.out.println("Reason: " + e.getMessage());
            }
        }
        return importedCards;
    }
}
