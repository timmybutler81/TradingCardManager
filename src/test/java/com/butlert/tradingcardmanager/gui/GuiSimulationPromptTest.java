package com.butlert.tradingcardmanager.gui;

import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import com.butlert.tradingcardmanager.utils.validation.ValidatorResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuiSimulationPromptTest {

    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    void promptAndValidate_invalidThenValidInput_shouldLoopAndReturnValid() {
        String input = "bad\nValidInput\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GuiSimulationPrompt prompt = new GuiSimulationPrompt(new CardValidator());
        Function<String, ValidatorResult> validator = val -> {
            if (val.length() >= 5) return ValidatorResult.success();
            return ValidatorResult.fail("Input too short");
        };

        String result = prompt.promptAndValidate("Enter input", validator);
        assertEquals("ValidInput", result);
    }

    @Test
    void promptOptional_blankInput_returnsCurrentValue() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GuiSimulationPrompt prompt = new GuiSimulationPrompt(new CardValidator());
        Function<String, ValidatorResult> validator = val -> ValidatorResult.success();

        String result = prompt.promptOptional("Enter value", "CurrentValue", validator);
        assertEquals("CurrentValue", result);
    }

    @Test
    void promptOptional_validInput_returnsNewValue() {
        String input = "NewValue\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GuiSimulationPrompt prompt = new GuiSimulationPrompt(new CardValidator());
        Function<String, ValidatorResult> validator = val -> {
            if (val.length() >= 3) return ValidatorResult.success();
            return ValidatorResult.fail("Too short");
        };

        String result = prompt.promptOptional("Enter value", "CurrentValue", validator);
        assertEquals("NewValue", result);
    }
}