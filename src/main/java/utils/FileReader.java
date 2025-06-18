package main.java.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {
    public List<String> readLines(String path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(path))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
            return List.of();
        }

    }

}
