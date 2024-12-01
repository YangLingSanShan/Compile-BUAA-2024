package io.Conponent;

import frontend.LexerAnalysis.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LexerPrinter implements output {
    private String outputPath;
    private BufferedWriter writer;

    private ArrayList<Token> Tokens;

    @Override
    public void init(String path, boolean control) {
        this.outputPath = path;
        try {
            this.writer = control ? new BufferedWriter(new FileWriter(this.outputPath, false)) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.Tokens = new ArrayList<>();
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
            for (Token token : this.Tokens) {
                writer.write(token.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToken(ArrayList<Token> tokens) {
        this.Tokens.addAll(tokens);
    }

    public ArrayList<Token> getTokens() {
        return Tokens;
    }
}
