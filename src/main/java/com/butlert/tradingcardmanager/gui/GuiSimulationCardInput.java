/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * GuiSimulationCardInput.java
 * This class simulates how the GUI handle card input. It simulates adding, removing and modifying a card record.
 */
package com.butlert.tradingcardmanager.gui;

import com.butlert.tradingcardmanager.controller.CardController;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GuiSimulationCardInput {
    private final CardController controller;
    private final CardValidator validator;
    private final GuiSimulationPrompt prompt;
    private final GuiSimulationCardDisplay display;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GuiSimulationCardInput(CardController controller, CardValidator validator, GuiSimulationPrompt prompt, GuiSimulationCardDisplay display) {
        this.controller = controller;
        this.validator = validator;
        this.prompt = prompt;
        this.display = display;
    }

    /**
     * method: simulateAddCard
     * parameters: none
     * return: void
     * purpose: Simulates adding a card to the system. It handles a lot of validator logic from the class and
     * uses the prompt and validate method to complete the add card.
     */
    public void simulateAddCard() {
        int id = Integer.parseInt(prompt.promptAndValidate(
                "Card ID", validator::validateCardNumber,
                input -> controller.getCardById(Integer.parseInt(input)) == null,
                "Duplicate ID"
        ));

        String game = prompt.promptAndValidate("Card Game", validator::stringValidator);
        String name = prompt.promptAndValidate("Card Name", validator::stringValidator);

        String rarityStr = prompt.promptAndValidate("Card Rarity (COMMON, RARE, HERO, LEGENDARY)",
                validator::validateCardRarity);
        if (rarityStr == null || rarityStr.isBlank()) {
            throw new IllegalArgumentException("Rarity must not be null or empty");
        }
        CardRarity rarity = CardRarity.valueOf(rarityStr.toUpperCase());

        LocalDate datePurchased = LocalDate.parse(prompt.promptAndValidate(
                "Date Purchased (yyyy-MM-dd)", s -> validator.dateValidator(s, dateTimeFormatter)));

        LocalDate dateSet = LocalDate.parse(prompt.promptAndValidate(
                "Date Set Published (yyyy-MM-dd)", s -> validator.dateValidator(s, dateTimeFormatter)));

        BigDecimal price = new BigDecimal(prompt.promptAndValidate("Purchase Price",
                validator::validatePurchasePrice));

        boolean foiled = Boolean.parseBoolean(prompt.promptAndValidate("Is Foiled (true/false)",
                validator::validateIsFoiled));

        controller.addCard(new Card(id, game, name, rarity, datePurchased, dateSet, price, foiled));
        System.out.println("Card added.");
        display.displayAllCards();
    }

    /**
     * method: simulateDeleteCard
     * parameters: none
     * return: void
     * purpose: Simulates removing a card from the system with some validation.
     */
    public void simulateDeleteCard() {
        int id = Integer.parseInt(prompt.promptAndValidate(
                "Card ID to delete", validator::validateCardNumber,
                input -> controller.getCardById(Integer.parseInt(input)) != null,
                "Card not found."
        ));
        controller.deleteCard(id);
        System.out.println("Card deleted.");
        display.displayAllCards();
    }

    /**
     * method: simulateModifyCard
     * parameters: none
     * return: void
     * purpose: Simulates modifying a card. It handles a lot of validator logic from the class. Uses the prompt and
     * validate method and prompt optional method to complete to prompt and display info needed and
     * current info already available.
     */
    public void simulateModifyCard() {
        int id = Integer.parseInt(prompt.promptAndValidate(
                "Card ID to modify", validator::validateCardNumber,
                input -> controller.getCardById(Integer.parseInt(input)) != null,
                "Card not found."
        ));
        Card card = controller.getCardById(id);

        System.out.println("Leave field blank to keep current.");

        String newGame = prompt.promptOptional("Card Game", card.getCardGame(), validator::stringValidator);
        String newName = prompt.promptOptional("Card Name", card.getCardName(), validator::stringValidator);

        String rarityStr = prompt.promptOptional("Rarity", card.getRarity().name(), validator::validateCardRarity);
        CardRarity newRarity = CardRarity.valueOf(rarityStr.toUpperCase());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate datePurchased = LocalDate.parse(prompt.promptOptional("Date Purchased", card.getDatePurchased().toString(),
                s -> validator.dateValidator(s, fmt)));

        LocalDate dateSet = LocalDate.parse(prompt.promptOptional("Date Set Published", card.getDateSetPublished().toString(),
                s -> validator.dateValidator(s, fmt)));

        BigDecimal price = new BigDecimal(prompt.promptOptional("Purchase Price", card.getPurchasePrice().toString(),
                validator::validatePurchasePrice));

        boolean foiled = Boolean.parseBoolean(prompt.promptOptional("Is Foiled", String.valueOf(card.isFoiled()),
                validator::validateIsFoiled));

        card.setCardGame(newGame);
        card.setCardName(newName);
        card.setRarity(newRarity);
        card.setDatePurchased(datePurchased);
        card.setDateSetPublished(dateSet);
        card.setPurchasePrice(price);
        card.setFoiled(foiled);

        System.out.println("Card updated.");
        display.displayAllCards();
    }
}
