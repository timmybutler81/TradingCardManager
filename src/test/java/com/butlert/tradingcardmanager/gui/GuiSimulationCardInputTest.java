package com.butlert.tradingcardmanager.gui;

import com.butlert.tradingcardmanager.controller.CardController;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GuiSimulationCardInputTest {

    private CardController controller;
    private CardValidator validator;
    private GuiSimulationPrompt prompt;
    private GuiSimulationCardDisplay display;
    private GuiSimulationCardInput input;

    @BeforeEach
    void setUp() {
        controller = mock(CardController.class);
        validator = mock(CardValidator.class);
        prompt = mock(GuiSimulationPrompt.class);
        display = mock(GuiSimulationCardDisplay.class);
        input = new GuiSimulationCardInput(controller, validator, prompt, display);
    }

    @Test
    void simulateAddCard_shouldAddValidCard() {
        when(prompt.promptAndValidate(eq("Card ID"), any(), any(), any())).thenReturn("1");
        when(prompt.promptAndValidate(eq("Card Game"), any(), any(), any())).thenReturn("FFTCG");
        when(prompt.promptAndValidate(eq("Card Name"), any(), any(), any())).thenReturn("Cloud");
        when(prompt.promptAndValidate(eq("Card Rarity (COMMON, RARE, HERO, LEGENDARY)"), any(), any(), any())).thenReturn("HERO");
        when(prompt.promptAndValidate(eq("Date Purchased (yyyy-MM-dd)"), any(), any(), any())).thenReturn("2023-01-01");
        when(prompt.promptAndValidate(eq("Date Set Published (yyyy-MM-dd)"), any(), any(), any())).thenReturn("2022-06-01");
        when(prompt.promptAndValidate(eq("Purchase Price"), any(), any(), any())).thenReturn("12.50");
        when(prompt.promptAndValidate(eq("Is Foiled (true/false)"), any(), any(), any())).thenReturn("true");

        input.simulateAddCard();

        verify(controller).addCard(argThat(card ->
                card.getCardNumber() == 1 &&
                        card.getCardGame().equals("FFTCG") &&
                        card.getCardName().equals("Cloud") &&
                        card.getRarity() == CardRarity.HERO &&
                        card.getDatePurchased().equals(LocalDate.of(2023, 1, 1)) &&
                        card.getDateSetPublished().equals(LocalDate.of(2022, 6, 1)) &&
                        card.getPurchasePrice().compareTo(new BigDecimal("12.50")) == 0 &&
                        card.isFoiled()
        ));

        verify(display).displayAllCards();
    }

    @Test
    void simulateDeleteCard_shouldDeleteExistingCard() {
        when(prompt.promptAndValidate(eq("Card ID to delete"), any(), any(), any()))
                .thenReturn("10");

        when(controller.getCardById(10)).thenReturn(new Card(
                10, "FFTCG", "Tifa", CardRarity.RARE,
                LocalDate.now(), LocalDate.now(), new BigDecimal("3.00"), false
        ));

        input.simulateDeleteCard();

        verify(controller).deleteCard(10);
        verify(display).displayAllCards();
    }

    @Test
    void simulateModifyCard_shouldUpdateCardFields() {
        Card card = new Card(42, "Pokemon", "Charmander", CardRarity.COMMON,
                LocalDate.of(2022, 1, 1), LocalDate.of(2021, 6, 1), new BigDecimal("2.00"), false);

        when(prompt.promptAndValidate(eq("Card ID to modify"), any(), any(), any())).thenReturn("42");
        when(controller.getCardById(42)).thenReturn(card);

        when(prompt.promptOptional(eq("Card Game"), any(), any())).thenReturn("Magic");
        when(prompt.promptOptional(eq("Card Name"), any(), any())).thenReturn("Liliana");
        when(prompt.promptOptional(eq("Rarity"), any(), any())).thenReturn("HERO");
        when(prompt.promptOptional(eq("Date Purchased"), any(), any())).thenReturn("2023-01-01");
        when(prompt.promptOptional(eq("Date Set Published"), any(), any())).thenReturn("2022-12-25");
        when(prompt.promptOptional(eq("Purchase Price"), any(), any())).thenReturn("6.75");
        when(prompt.promptOptional(eq("Is Foiled"), any(), any())).thenReturn("true");

        input.simulateModifyCard();

        assertEquals("Magic", card.getCardGame());
        assertEquals("Liliana", card.getCardName());
        assertEquals(CardRarity.HERO, card.getRarity());
        assertEquals(LocalDate.of(2023, 1, 1), card.getDatePurchased());
        assertEquals(LocalDate.of(2022, 12, 25), card.getDateSetPublished());
        assertEquals(new BigDecimal("6.75"), card.getPurchasePrice());
        assertTrue(card.isFoiled());

        verify(display).displayAllCards();
    }
}
