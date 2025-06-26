package com.butlert.tradingcardmanager.gui;

import com.butlert.tradingcardmanager.controller.CardController;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuiSimulationFileImportTest {

    private CardController controller;
    private GuiSimulationFileImport fileImport;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        controller = mock(CardController.class);
        fileImport = new GuiSimulationFileImport(controller);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void simulateFileImport_shouldPrintCardDetails_whenValidFilePathGiven() {
        File tempFile = new File("temp-card-file.txt");
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.println("dummy"); // content doesn't matter for this test
        } catch (IOException e) {
            fail("Failed to create temp file.");
        }

        Card mockCard = new Card(1, "FFTCG", "Cloud", CardRarity.HERO,
                LocalDate.now(), LocalDate.now(), new BigDecimal("5.0"), true);
        when(controller.importCardsFromFile(tempFile.getPath())).thenReturn(List.of(mockCard));
        System.setIn(new ByteArrayInputStream((tempFile.getPath() + System.lineSeparator()).getBytes()));
        fileImport.simulateFileImport();

        String output = outContent.toString();
        assertTrue(output.contains("Imported 1 cards:"));
        assertTrue(output.contains("Cloud"));
        tempFile.delete();
    }

    @Test
    void simulateFileImport_shouldPrintError_whenFileIsBlank() {
        System.setIn(new ByteArrayInputStream((System.lineSeparator()).getBytes()));
        fileImport.simulateFileImport();
        assertTrue(outContent.toString().contains("Invalid file path: Path is blank."));
    }

    @Test
    void simulateFileImport_shouldPrintError_whenFileDoesNotExist() {
        System.setIn(new ByteArrayInputStream(("nonexistentfile.txt" + System.lineSeparator()).getBytes()));
        fileImport.simulateFileImport();
        assertTrue(outContent.toString().contains("Invalid file path: File does not exist or is a directory."));
    }
}