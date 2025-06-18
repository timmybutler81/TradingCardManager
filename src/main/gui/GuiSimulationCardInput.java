package main.gui;

import main.java.controller.CardController;
import main.java.db.Card;
import main.java.utils.CardRarity;
import main.java.utils.CardValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GuiSimulationCardInput {
    private final CardController controller;
    private final CardValidator validator;
    private final GuiSimulationPrompt prompt;

    public GuiSimulationCardInput(CardController controller, CardValidator validator, GuiSimulationPrompt prompt) {
        this.controller = controller;
        this.validator = validator;
        this.prompt = prompt;
    }

    public void simulateAddCard() {
        int id = Integer.parseInt(prompt.promptAndValidate(
                "Card ID", validator::validateCardNumber,
                input -> controller.getCardById(Integer.parseInt(input)) == null,
                "Duplicate ID"
        ));

        String game = prompt.promptAndValidate("Card Game", validator::stringValidator, null, null);
        String name = prompt.promptAndValidate("Card Name", validator::stringValidator, null, null);

        String rarityStr = prompt.promptAndValidate("Card Rarity (COMMON, RARE, HERO, LEGENDARY)",
                validator::validateCardRarity, null, null);
        CardRarity rarity = CardRarity.valueOf(rarityStr.toUpperCase());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate datePurchased = LocalDate.parse(prompt.promptAndValidate(
                "Date Purchased (yyyy-MM-dd)", s -> validator.dateValidator(s, fmt), null, null));

        LocalDate dateSet = LocalDate.parse(prompt.promptAndValidate(
                "Date Set Published (yyyy-MM-dd)", s -> validator.dateValidator(s, fmt), null, null));

        BigDecimal price = new BigDecimal(prompt.promptAndValidate("Purchase Price",
                validator::validatePurchasePrice, null, null));

        boolean foiled = Boolean.parseBoolean(prompt.promptAndValidate("Is Foiled (true/false)",
                validator::validateIsFoiled, null, null));

        controller.addCard(new Card(id, game, name, rarity, datePurchased, dateSet, price, foiled));
        System.out.println("Card added.");
    }

    public void simulateDeleteCard() {
        int id = Integer.parseInt(prompt.promptAndValidate(
                "Card ID to delete", validator::validateCardNumber,
                input -> controller.getCardById(Integer.parseInt(input)) != null,
                "Card not found."
        ));
        controller.deleteCard(id);
        System.out.println("Card deleted.");
    }

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
    }
}