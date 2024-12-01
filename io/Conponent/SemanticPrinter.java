package io.Conponent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SemanticPrinter implements output {

    private ArrayList<String> lines;
    private String outputPath;

    private BufferedWriter writer;

    @Override
    public void init(String path, boolean control) {
        this.outputPath = path;
        try {
            this.writer = control ? new BufferedWriter(new FileWriter(this.outputPath, false)) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.lines = new ArrayList<>();
    }

    @Override
    public BufferedWriter getOutputBuffer() {
        return writer;
    }

    @Override
    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void print() {
        try {
            for (String line : this.lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLine(String s) {
        this.lines.add(s);
    }
}
