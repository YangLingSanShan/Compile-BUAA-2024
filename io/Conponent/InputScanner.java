package io.Conponent;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputScanner {

    private BufferedReader reader;
    private final String inputPath;

    public InputScanner(boolean fileInputController, String inputPath) {
        this.inputPath = inputPath;
        try {
            reader = new BufferedReader(fileInputController ? new FileReader(this.inputPath) : new InputStreamReader(System.in));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedReader getReader() {
        return reader;
    }
}
