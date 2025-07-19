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

/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * ReadTextFile.java
 *
 * This class is responsible for reading a text file line-by-line and returning the contents as a list of strings.
 * It abstracts file I/O operations to make future transitions to GUI-based file input seamless and isolated.
 */
@Component
public class ReadTextFile {

    /**
     * Logger instance for recording application events and errors in the
     * {@link com.butlert.tradingcardmanager.controller.DatabaseConnectionController}.
     * <p>
     * Uses SLF4J's {@link org.slf4j.LoggerFactory} for standardized logging.
     * Helps in debugging and tracing database connection handling.
     */
    private static final Logger logger = Logger.getLogger(ReadTextFile.class.getName());

    /**
     * Constructs a {@code ReadTextFile} component.
     * <p>This default constructor is provided for Spring to instantiate this utility as a component.</p>
     */
    public ReadTextFile() {

    }

    /**
     * Reads all lines from a given file path and returns them as a list of strings.
     *
     * @param filePath the path to the text file
     * @return a list of strings, each representing a line from the file
     * @throws CardImportException if the file cannot be read
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

