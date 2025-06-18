package main;

import main.java.controller.CardController;
import main.java.db.CardRepository;
import main.java.db.CardRepositoryImpl;
import main.java.service.CardService;
import main.java.service.CardServiceImpl;
import main.java.utils.CardParser;
import main.java.utils.CardValidator;
import main.java.utils.DateParser;
import main.java.utils.FileReader;
import main.gui.*;

public class Main {
    public static void main(String[] args) {
        // Create the required dependencies
        CardRepository repository = new CardRepositoryImpl();
        CardValidator validator = new CardValidator();
        DateParser dateParser = new DateParser();
        CardParser cardParser = new CardParser(dateParser, validator);
        FileReader fileReader = new FileReader();

        // Inject into CardServiceImpl
        CardService cardService = new CardServiceImpl(repository, cardParser, fileReader);
        CardController controller = new CardController(cardService);
        GuiSimulationPrompt prompt = new GuiSimulationPrompt();

        // Create simulation modules
        GuiSimulationCardInput input = new GuiSimulationCardInput(controller, validator, prompt);
        GuiSimulationCardDisplay display = new GuiSimulationCardDisplay(controller);
        GuiSimulationFileImport fileImport = new GuiSimulationFileImport(controller);

        // Start main menu
        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(input, display, fileImport);
        menu.start();
    }
}
