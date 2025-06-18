package main.gui;

import java.util.Scanner;

public class GuiSimulationMainMenu {
    private final GuiSimulationCardInput cardInput;
    private final GuiSimulationCardDisplay cardDisplay;
    private final GuiSimulationFileImport fileImport;

    public GuiSimulationMainMenu(
            GuiSimulationCardInput cardInput,
            GuiSimulationCardDisplay cardDisplay,
            GuiSimulationFileImport fileImport
    ) {
        this.cardInput = cardInput;
        this.cardDisplay = cardDisplay;
        this.fileImport = fileImport;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;

        System.out.println("Welcome to the Trading Card Manager");

        while (selection != 8) {
            System.out.println("""
                \nPlease type the number of the action you want to take:
                1. Add Card
                2. Remove Card
                3. Modify Card
                4. Display all Cards
                5. Display Card Statistics
                6. Calculate Total Collection Value
                7. Load Cards from File
                8. Exit""");

            String input = scanner.nextLine();
            try {
                selection = Integer.parseInt(input);
                switch (selection) {
                    case 1 -> cardInput.simulateAddCard();
                    case 2 -> cardInput.simulateDeleteCard();
                    case 3 -> cardInput.simulateModifyCard();
                    case 4 -> cardDisplay.displayAllCards();
                    case 5 -> cardDisplay.displayCardStatistics();
                    case 6 -> cardDisplay.simulateCalculateTotalCollectionValue();
                    case 7 -> fileImport.simulateFileImport();
                    case 8 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid option. Enter 1–8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number between 1 and 8.");
            }
        }
    }
}

