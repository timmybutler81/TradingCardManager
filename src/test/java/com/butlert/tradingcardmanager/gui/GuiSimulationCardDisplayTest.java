package com.butlert.tradingcardmanager.gui;

import com.butlert.tradingcardmanager.controller.CardController;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GuiSimulationCardDisplayTest {

    private CardController controller;
    private GuiSimulationCardDisplay display;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        controller = mock(CardController.class);
        display = new GuiSimulationCardDisplay(controller);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void displayAllCards_shouldPrintCards() {
        Card card1 = new Card(1, "FFTCG", "Cloud", CardRarity.HERO,
                LocalDate.of(2023, 1, 1), LocalDate.of(2022, 6, 1), new BigDecimal("15.00"), true);

        Card card2 = new Card(2, "Pokemon", "Pikachu", CardRarity.RARE,
                LocalDate.of(2022, 3, 15), LocalDate.of(2021, 9, 1), new BigDecimal("8.00"), false);

        when(controller.getAllCards()).thenReturn(List.of(card1, card2));

        display.displayAllCards();

        String output = outContent.toString();
        assertTrue(output.contains("Cloud"));
        assertTrue(output.contains("Pikachu"));
    }

    @Test
    void displayAllCards_shouldPrintEmptyMessage() {
        when(controller.getAllCards()).thenReturn(List.of());

        display.displayAllCards();

        String output = outContent.toString();
        assertTrue(output.contains("No cards available."));
    }
}
