package com.butlert.tradingcardmanager.utils;

import com.butlert.tradingcardmanager.utils.exception.CardImportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReadTextFileTest {

    private ReadTextFile reader;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        reader = new ReadTextFile();
        tempFile = Files.createTempFile("test-file", ".txt");
        Files.write(tempFile, List.of("Line 1", "Line 2", "Line 3"));
    }

    @Test
    void readLines_validFile_returnsLines() {
        List<String> lines = reader.readLines(tempFile.toString());
        assertEquals(3, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
        assertEquals("Line 3", lines.get(2));
    }

    @Test
    void readLines_nonexistentFile_throwsCardImportException() {
        ReadTextFile reader = new ReadTextFile();

        assertThrows(CardImportException.class, () -> {
            reader.readLines("C:\\Users\\Tim\\AppData\\Local\\Temp\\nonexistent.txt");
        });
    }
}
