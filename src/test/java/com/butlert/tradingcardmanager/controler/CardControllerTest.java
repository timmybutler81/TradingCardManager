package com.butlert.tradingcardmanager.controler;

import com.butlert.tradingcardmanager.controller.CardController;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardControllerTest {

    private CardService service;
    private CardController controller;

    @BeforeEach
    void setUp() {
        service = mock(CardService.class);
        controller = new CardController(service);
    }

    @Test
    void addCard_shouldCallService() {
        Card card = mock(Card.class);
        when(service.addCard(card)).thenReturn(true);

        assertTrue(controller.addCard(card));
        verify(service).addCard(card);
    }

    @Test
    void deleteCard_shouldCallService() {
        controller.deleteCard(42);
        verify(service).deleteCard(42);
    }

    @Test
    void getAllCards_shouldReturnCards() {
        List<Card> expected = List.of(mock(Card.class));
        when(service.getAllCards()).thenReturn(expected);

        assertEquals(expected, controller.getAllCards());
    }

    @Test
    void getCardById_shouldReturnCard() {
        Card expected = mock(Card.class);
        when(service.findByCardId(101)).thenReturn(expected);

        assertEquals(expected, controller.getCardById(101));
    }

    @Test
    void calculateCollectionStatistics_shouldReturnStats() {
        Map<String, Object> stats = Map.of("totalCards", 5);
        when(service.calculateCollectionStatistics()).thenReturn(stats);

        assertEquals(stats, controller.calculateCollectionStatistics());
    }

    @Test
    void calculateCollectionValue_shouldReturnValueMap() {
        Map<String, BigDecimal> value = Map.of("owner", new BigDecimal("100.00"));
        when(service.calculateCollectionValues()).thenReturn(value);

        assertEquals(value, controller.calculateCollectionValue());
    }

    @Test
    void importCardsFromFile_shouldReturnCards() {
        List<Card> cards = List.of(mock(Card.class));
        when(service.addAllCardsFromFile("input.txt")).thenReturn(cards);

        assertEquals(cards, controller.importCardsFromFile("input.txt"));
    }
}
