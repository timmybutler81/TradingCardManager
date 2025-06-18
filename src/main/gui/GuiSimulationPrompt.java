package main.gui;

import java.util.Scanner;
import java.util.function.Predicate;

public class GuiSimulationPrompt {
    private final Scanner scanner = new Scanner(System.in);

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

    public String promptOptional(String prompt, String current, Predicate<String> validator) {
        while (true) {
            System.out.println(prompt + " [" + current + "]:");
            String input = scanner.nextLine();
            if (input.isBlank()) return current;
            if (validator.test(input)) return input;
        }
    }
}
