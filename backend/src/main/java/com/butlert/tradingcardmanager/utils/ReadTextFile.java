/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * DateParser.java
 * This class is used to read the file and ensure that it can read the file. Its abstracted away for future changes
 * when we need to read the file from a GUI instead of the cli.
 */
package com.butlert.tradingcardmanager.utils;

import com.butlert.tradingcardmanager.utils.exception.CardImportException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class ReadTextFile {

    private static final Logger logger = Logger.getLogger(ReadTextFile.class.getName());

    /**
     * method: readLines
     * parameters: path
     * return: List of Strings
     * purpose: returns a list of strings from it read from the file
     */
    public List<String> readLines(String filePath) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(filePath))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to import cards from file: " + filePath, e);
            throw new CardImportException("Unable to import cards: " + e.getMessage(), e);
        }

    }

}

