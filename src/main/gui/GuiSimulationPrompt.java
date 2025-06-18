/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * GuiSimulationPrompt.java
 * This class simulates how the GUI prompts are handled from each option selected from the main menu. Selections
 * pass through this class for providing prompts and validation in a generic way that is reusable.
 */
package main.gui;

import java.util.Scanner;
import java.util.function.Predicate;

public class GuiSimulationPrompt {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * method: promptAndValidate
     * parameters: prompt, validator, additional check, failMessage
     * return: String
     * purpose: Serves as a way to call prompting and validating used in add card to take multiple parameters
     *          and be reusable for all card fields. It is why all validations return booleans
     */
    public String promptAndValidate(String prompt, Predicate<String> validator,
                                    Predicate<String> additionalCheck, String failMessage) {
        while (true) {
            System.out.println(prompt + ":");
            String input = scanner.nextLine();
            if (!validator.test(input)) continue;
            if (additionalCheck != null && !additionalCheck.test(input)) {
                if (failMessage != null) System.out.println(failMessage);
                continue;
            }
            return input;
        }
    }

    /**
     * method: promptAndValidate
     * parameters: prompt, current, validator
     * return: String
     * purpose: Serves as a way to prompt for the input with the value already present when in the modify flow
     *          It is reusable for all card fields. It is why all validations return booleans
     */
    public String promptOptional(String prompt, String current, Predicate<String> validator) {
        while (true) {
            System.out.println(prompt + " [" + current + "]:");
            String input = scanner.nextLine();
            if (input.isBlank()) return current;
            if (validator.test(input)) return input;
        }
    }
}
