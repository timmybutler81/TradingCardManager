/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * DateParser.java
 * This class is used to read the file and ensure that it can read the file. Its abstracted away for future changes
 * when we need to read the file from a GUI instead of the cli.
 */
package main.java.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {

    /**
     * method: readLines
     * parameters: path
     * return: List of Strings
     * purpose: returns a list of strings from it read from the file
     */
    public List<String> readLines(String path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(path))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
            return List.of();
        }

    }

}
