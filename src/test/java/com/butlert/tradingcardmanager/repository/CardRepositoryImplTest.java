package com.butlert.tradingcardmanager.repository;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardRepositoryImplTest {

    private CardRepositoryImpl repository;
    private Card sampleCard;

    @BeforeEach
    void setUp() {
        repository = new CardRepositoryImpl();
        sampleCard = new Card(1, "FFTCG", "Cloud", CardRarity.HERO,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2022, 6, 1),
                new BigDecimal("12.50"),
                true);
    }

    @Test
    void saveCard_shouldAddCard() {
        assertTrue(repository.saveCard(sampleCard));
        assertEquals(1, repository.findAllCards().size());
    }

    @Test
    void saveCard_shouldRejectDuplicate() {
        repository.saveCard(sampleCard);
        boolean result = repository.saveCard(sampleCard);
        assertFalse(result);
        assertEquals(1, repository.findAllCards().size());
    }

    @Test
    void deleteCard_shouldRemoveCard() {
        repository.saveCard(sampleCard);
        boolean result = repository.deleteCard(sampleCard.getCardNumber());
        assertTrue(result);
        assertEquals(0, repository.findAllCards().size());
    }

    @Test
    void deleteCard_shouldFailIfNotFound() {
        boolean result = repository.deleteCard(99);
        assertFalse(result);
    }

    @Test
    void findAllCards_shouldReturnAll() {
        repository.saveCard(sampleCard);
        Card another = new Card(2, "Pokemon", "Pikachu", CardRarity.RARE,
                LocalDate.now(), LocalDate.now(), new BigDecimal("5.00"), false);
        repository.saveCard(another);

        List<Card> cards = repository.findAllCards();
        assertEquals(2, cards.size());
    }

    @Test
    void findByCardId_shouldReturnMatch() {
        repository.saveCard(sampleCard);
        Card found = repository.findByCardId(1);
        assertNotNull(found);
        assertEquals("Cloud", found.getCardName());
    }

    @Test
    void findByCardId_shouldReturnNullIfNotFound() {
        Card found = repository.findByCardId(999);
        assertNull(found);
    }
}
