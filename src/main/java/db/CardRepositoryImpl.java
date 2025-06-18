package main.java.db;

import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {
    private List<Card> cardList = new ArrayList<>();

    @Override
    public boolean saveCard(Card card) {
        if (findByCardId(card.getCardNumber()) != null) {
            System.out.println("Card with ID " + card.getCardNumber() + " already exists.");
            return false;
        }
        cardList.add(card);
        return true;
    }

    @Override
    public boolean deleteCard(int cardId) {
        Card card = findByCardId(cardId);
        if (card != null) {
            cardList.remove(card);
            return true;
        }
        return false;
    }

    @Override
    public List<Card> findAllCards() {
        return List.copyOf(cardList);
    }

    @Override
    public Card findByCardId(int cardId) {
        return cardList.stream()
                .filter(card -> card.getCardNumber() == cardId)
                .findFirst()
                .orElse(null);
    }
}
