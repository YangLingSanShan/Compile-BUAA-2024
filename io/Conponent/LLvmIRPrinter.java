package io.Conponent;

import frontend.LexerAnalysis.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LLvmIRPrinter implements output {

    private String outputPath;
    private BufferedWriter writer;

    private String llvmIRcode;

    @Override
    public void init(String path, boolean control) {
        this.outputPath = path;
        try {
            this.writer = control ? new BufferedWriter(new FileWriter(this.outputPath, false)) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.llvmIRcode = "";
    }

    @Override
    public BufferedWriter getOutputBuffer() {
        return this.writer;
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
            writer.write(llvmIRcode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLLvmIROutput(String string) {
        this.llvmIRcode = string;
    }
}
