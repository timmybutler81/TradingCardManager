/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * GuiSimulationFileImport.java
 * This class simulates how the GUI handles a file import. It asks for the file path and parses it and
 * calls the simulation prompt class.
 */
package main.gui;

import main.java.controller.CardController;
import main.java.db.Card;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class GuiSimulationFileImport {
    private final CardController controller;

    public GuiSimulationFileImport(CardController controller) {
        this.controller = controller;
    }

    /**
     * method: simulateFileImport
     * parameters: none
     * return: void
     * purpose: Simulates how the GUI will handle importing a file to check if the file is in the directory
     *          and verify the file is not empty.
     */
    public void simulateFileImport() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to the card file:");
        String filePath = scanner.nextLine();

        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("Invalid file path: Path is blank.");
            return;
        }

        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Invalid file path: File does not exist or is a directory.");
            return;
        }

        List<Card> cards = controller.importCardsFromFile(filePath);
        if (cards.isEmpty()) {
            System.out.println("No cards imported.");
        } else {
            System.out.println("Imported " + cards.size() + " cards:");
            cards.forEach(System.out::println);
        }
    }
}
