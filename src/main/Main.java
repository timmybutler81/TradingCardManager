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
        CardRepository repository = new CardRepositoryImpl();
        CardValidator validator = new CardValidator();
        DateParser dateParser = new DateParser();
        CardParser cardParser = new CardParser(dateParser, validator);
        FileReader fileReader = new FileReader();

        CardService cardService = new CardServiceImpl(repository, cardParser, fileReader);
        CardController controller = new CardController(cardService);
        GuiSimulationPrompt prompt = new GuiSimulationPrompt();

        GuiSimulationCardDisplay display = new GuiSimulationCardDisplay(controller);
        GuiSimulationFileImport fileImport = new GuiSimulationFileImport(controller);
        GuiSimulationCardInput input = new GuiSimulationCardInput(controller, validator, prompt, display);

        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(input, display, fileImport);
        menu.start();
    }
}
