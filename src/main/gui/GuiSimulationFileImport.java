package main.gui;

import main.java.controller.CardController;
import main.java.db.Card;

import java.util.List;
import java.util.Scanner;

public class GuiSimulationFileImport {
    private final CardController controller;

    public GuiSimulationFileImport(CardController controller) {
        this.controller = controller;
    }

    public void simulateFileImport() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to the card file:");
        String path = scanner.nextLine();

        List<Card> cards = controller.importCardsFromFile(path);
        if (cards.isEmpty()) {
            System.out.println("No cards imported.");
        } else {
            System.out.println("Imported " + cards.size() + " cards:");
            cards.forEach(System.out::println);
        }
    }
}
