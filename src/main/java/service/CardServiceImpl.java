package main.java.service;

import main.java.db.Card;
import main.java.db.CardRepository;
import main.java.db.CardRepositoryImpl;
import main.java.utils.CardDateUtil;
import main.java.utils.CardParser;
import main.java.utils.CardRarity;
import main.java.utils.FileReader;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Override
    public boolean addCard(Card card) {
        return cardRepository.saveCard(card);
    }

    @Override
    public boolean deleteCard(int cardId) {
        cardRepository.deleteCard(cardId);
        return false;
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAllCards();
    }

    @Override
    public Card findByCardId(int cardId) {
        return cardRepository.findByCardId(cardId);
    }

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
            long marketIntervals = cardDateUtil.calculateThirtyDayIntervals(card.getDateSetPublished());
            long ownerIntervals = cardDateUtil.calculateThirtyDayIntervals(card.getDatePurchased());

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
