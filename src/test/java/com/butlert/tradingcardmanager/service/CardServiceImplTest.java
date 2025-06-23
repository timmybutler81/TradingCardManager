package com.butlert.tradingcardmanager.service;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.repository.CardRepository;
import com.butlert.tradingcardmanager.utils.CardParser;
import com.butlert.tradingcardmanager.utils.ReadTextFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceImplTest {

    private CardRepository repository;
    private CardParser parser;
    private ReadTextFile reader;
    private CardServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(CardRepository.class);
        parser = mock(CardParser.class);
        reader = mock(ReadTextFile.class);
        service = new CardServiceImpl(repository, parser, reader);
    }

    @Test
    void addCard_savesSuccessfully() {
        Card card = mock(Card.class);
        when(repository.saveCard(card)).thenReturn(true);
        assertTrue(service.addCard(card));
        verify(repository).saveCard(card);
    }

    @Test
    void deleteCard_callsRepository() {
        service.deleteCard(1);
        verify(repository).deleteCard(1);
    }

    @Test
    void getAllCards_returnsCards() {
        List<Card> cards = List.of(mock(Card.class));
        when(repository.findAllCards()).thenReturn(cards);
        assertEquals(cards, service.getAllCards());
    }

    @Test
    void findByCardId_returnsCard() {
        Card card = mock(Card.class);
        when(repository.findByCardId(42)).thenReturn(card);
        assertEquals(card, service.findByCardId(42));
    }

    @Test
    void calculateCollectionStatistics_returnsExpectedStats() {
        Card card1 = new Card(1, "Game", "Name", CardRarity.COMMON, LocalDate.now(), LocalDate.now(), new BigDecimal("10.00"), true);
        Card card2 = new Card(2, "Game", "Name", CardRarity.RARE, LocalDate.now(), LocalDate.now(), new BigDecimal("20.00"), false);

        when(repository.findAllCards()).thenReturn(List.of(card1, card2));

        Map<String, Object> stats = service.calculateCollectionStatistics();

        assertEquals(new BigDecimal("30.00"), stats.get("totalSpent"));
        assertEquals(2, stats.get("totalCards"));
        assertEquals(1, stats.get("totalFoiled"));
        assertEquals(50.0, (double) stats.get("percentFoiled"));
    }

    @Test
    void addAllCardsFromFile_importsValidCards() {
        String filePath = "fake/path.txt";
        String line = "some valid line";

        Card card = new Card(1, "Game", "Name", CardRarity.HERO, LocalDate.now(), LocalDate.now(), new BigDecimal("5.00"), false);
        when(reader.readLines(filePath)).thenReturn(List.of(line));
        when(parser.parseLine(line)).thenReturn(Optional.of(card));
        when(repository.saveCard(card)).thenReturn(true);

        List<Card> result = service.addAllCardsFromFile(filePath);
        assertEquals(1, result.size());
        assertEquals(card, result.get(0));
    }
}
