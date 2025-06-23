package com.butlert.tradingcardmanager;

import com.butlert.tradingcardmanager.controller.CardController;
import com.butlert.tradingcardmanager.gui.*;
import com.butlert.tradingcardmanager.repository.CardRepository;
import com.butlert.tradingcardmanager.repository.CardRepositoryImpl;
import com.butlert.tradingcardmanager.service.CardService;
import com.butlert.tradingcardmanager.service.CardServiceImpl;
import com.butlert.tradingcardmanager.utils.CardParser;
import com.butlert.tradingcardmanager.utils.DateParser;
import com.butlert.tradingcardmanager.utils.ReadTextFile;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;

public class Main {
    public static void main(String[] args) {
        CardRepository repository = new CardRepositoryImpl();
        CardValidator validator = new CardValidator();
        DateParser dateParser = new DateParser();
        CardParser cardParser = new CardParser(dateParser, validator);
        ReadTextFile readTextFile = new ReadTextFile();

        CardService cardService = new CardServiceImpl(repository, cardParser, readTextFile);
        CardController controller = new CardController(cardService);
        GuiSimulationPrompt prompt = new GuiSimulationPrompt();

        GuiSimulationCardDisplay display = new GuiSimulationCardDisplay(controller);
        GuiSimulationFileImport fileImport = new GuiSimulationFileImport(controller);
        GuiSimulationCardInput input = new GuiSimulationCardInput(controller, validator, prompt, display);

        GuiSimulationMainMenu menu = new GuiSimulationMainMenu(input, display, fileImport);
        menu.start();
    }
}
