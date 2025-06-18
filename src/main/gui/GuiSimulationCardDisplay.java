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

    public void displayCardStatistics() {
        Map<String, Object> stats = controller.calculateCollectionStatistics();
        System.out.println("==== Collection Statistics ====");
        stats.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    public void simulateCalculateTotalCollectionValue() {
        Map<String, BigDecimal> values = controller.calculateCollectionValue();
        System.out.println("==== Collection Value ====");
        System.out.println("Market Value: " + formatAsCurrency(values.get("marketValue")));
        System.out.println("Your Value: " + formatAsCurrency(values.get("ownerValue")));
    }

    private String formatAsCurrency(BigDecimal value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(value);
    }
}
