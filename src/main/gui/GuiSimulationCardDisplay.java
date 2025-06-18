/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * GuiSimulationCardDisplay.java
 * This class simulates how the GUI will display cards and the statistics about the cards
 */

package main.gui;

import main.java.controller.CardController;
import main.java.db.Card;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GuiSimulationCardDisplay {
    private final CardController controller;

    public GuiSimulationCardDisplay(CardController controller) {
        this.controller = controller;
    }

    /**
     * method: displayAllCards
     * parameters: none
     * return: void
     * purpose: Simulates displaying all cards to the CLI that would normally appear in the GUI
     */
    public void displayAllCards() {
        List<Card> cards = controller.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("No cards available.");
            return;
        }
        for (Card card : cards) {
            System.out.println(card);
        }
    }

    /**
     * method: displayCardStatistics
     * parameters: none
     * return: void
     * purpose: Simulates displaying card stats to the CLI that would normally appear in the GUI
     */
    public void displayCardStatistics() {
        Map<String, Object> stats = controller.calculateCollectionStatistics();
        System.out.println("==== Collection Statistics ====");
        stats.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    /**
     * method: simulateCalculateTotalCollectionValue
     * parameters: none
     * return: void
     * purpose: Simulates calculation display to the CLI that would normally appear on the GUI
     */
    public void simulateCalculateTotalCollectionValue() {
        Map<String, BigDecimal> values = controller.calculateCollectionValue();
        System.out.println("==== Collection Value ====");
        System.out.println("Market Value: " + formatAsCurrency(values.get("marketValue")));
        System.out.println("Your Value: " + formatAsCurrency(values.get("ownerValue")));
    }

    /**
     * method: formatAsCurrency
     * parameters: value
     * return: String
     * purpose: Method to format currency by US standard
     */
    private String formatAsCurrency(BigDecimal value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(value);
    }
}
