package com.butlert.tradingcardmanager.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class GuiSimulationMainMenuTest {

    private final InputStream originalIn = System.in;

    private GuiSimulationCardInput mockCardInput;
    private GuiSimulationCardDisplay mockCardDisplay;
    private GuiSimulationFileImport mockFileImport;

    @BeforeEach
    void setUp() {
        mockCardInput = mock(GuiSimulationCardInput.class);
        mockCardDisplay = mock(GuiSimulationCardDisplay.class);
        mockFileImport = mock(GuiSimulationFileImport.class);
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    void start_addCardOption_callsSimulateAddCard() {
        String simulatedInput = "1\n8\n"; // Add Card, then Exit
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        verify(mockCardInput).simulateAddCard();
    }

    @Test
    void start_removeCardOption_callsSimulateDeleteCard() {
        System.setIn(new ByteArrayInputStream("2\n8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        verify(mockCardInput).simulateDeleteCard();
    }

    @Test
    void start_modifyCardOption_callsSimulateModifyCard() {
        System.setIn(new ByteArrayInputStream("3\n8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        verify(mockCardInput).simulateModifyCard();
    }

    @Test
    void start_displayCardsOption_callsDisplayAllCards() {
        System.setIn(new ByteArrayInputStream("4\n8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        verify(mockCardDisplay).displayAllCards();
    }

    @Test
    void start_displayStatisticsOption_callsDisplayCardStatistics() {
        System.setIn(new ByteArrayInputStream("5\n8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        verify(mockCardDisplay).displayCardStatistics();
    }

    @Test
    void start_calculateValueOption_callsSimulateCalculateTotalCollectionValue() {
        System.setIn(new ByteArrayInputStream("6\n8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        verify(mockCardDisplay).simulateCalculateTotalCollectionValue();
    }

    @Test
    void start_importCardsOption_callsSimulateFileImport() {
        System.setIn(new ByteArrayInputStream("7\n8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        verify(mockFileImport).simulateFileImport();
    }

    @Test
    void start_invalidOption_printsMessageAndLoops() {
        System.setIn(new ByteArrayInputStream("9\n8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();

        // No exception means it handled invalid input
    }

    @Test
    void start_quitOption_exitsWithoutError() {
        System.setIn(new ByteArrayInputStream("8\n".getBytes()));
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(mockCardInput, mockCardDisplay, mockFileImport);
        menu.start();
    }
}
