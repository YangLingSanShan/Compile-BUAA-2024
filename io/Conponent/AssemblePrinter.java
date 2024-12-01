package io.Conponent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AssemblePrinter implements output {

    private String outputPath;
    private BufferedWriter writer;

    private String assembleCode;

    @Override
    public void init(String path, boolean control) {
        this.outputPath = path;
        try {
            this.writer = control ? new BufferedWriter(new FileWriter(this.outputPath, false)) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.assembleCode = "";
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
            writer.write(assembleCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAssembleOutput(String string) {
        this.assembleCode = string;
    }
}
