package main.java.db;

import java.util.List;

public interface CardRepository {
    public boolean saveCard(Card card);
    public boolean deleteCard(int cardId);
    public List<Card> findAllCards();
    public Card findByCardId(int cardId);
}
