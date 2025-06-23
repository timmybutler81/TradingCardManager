package com.butlert.tradingcardmanager.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuiSimulationPromptTest {

    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        // Nothing needed for setup here
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);  // Restore original System.in after each test
    }

    @Test
    void promptAndValidate_validWithCondition_shouldPassBothChecks() {
        String input = "TestInput\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GuiSimulationPrompt prompt = new GuiSimulationPrompt();
        Predicate<String> validator = val -> val.length() > 3;
        Predicate<String> additionalCheck = val -> val.startsWith("T");

        String result = prompt.promptAndValidate("Enter input", validator, additionalCheck, "Must start with T");
        assertEquals("TestInput", result);
    }

    @Test
    void promptAndValidate_invalidThenValidInput_shouldLoopAndReturnValid() {
        String input = "bad\nValidInput\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GuiSimulationPrompt prompt = new GuiSimulationPrompt();
        Predicate<String> validator = val -> val.length() >= 5;

        String result = prompt.promptAndValidate("Enter input", validator, null, null);
        assertEquals("ValidInput", result);
    }

    @Test
    void promptOptional_blankInput_returnsCurrentValue() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GuiSimulationPrompt prompt = new GuiSimulationPrompt();
        String result = prompt.promptOptional("Enter value", "CurrentValue", val -> true);
        assertEquals("CurrentValue", result);
    }

    @Test
    void promptOptional_validInput_returnsNewValue() {
        String input = "NewValue\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GuiSimulationPrompt prompt = new GuiSimulationPrompt();
        String result = prompt.promptOptional("Enter value", "CurrentValue", val -> val.length() >= 3);
        assertEquals("NewValue", result);
    }
}