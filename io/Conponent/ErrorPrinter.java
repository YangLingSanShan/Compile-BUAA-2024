package io.Conponent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ErrorPrinter implements output {

    private String outputPath;
    private BufferedWriter writer;

    private ArrayList<ErrorToken> errorTokens;

    private ArrayList<String> errorInfos = new ArrayList<>();

    private boolean otherOutput = false;

    private boolean control;

    @Override
    public void init(String path, boolean control) {
        this.outputPath = path;
        this.control = control;
        this.otherOutput = false;
        try {
            this.writer = control ? new BufferedWriter(new FileWriter(this.outputPath, false)) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.errorTokens = new ArrayList<>();
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
        if (this.control) {
            try {
                this.errorTokens.sort(Comparator.comparingInt(ErrorToken::getLineNum));
                for (ErrorToken token : this.errorTokens) {
                    writer.write(token.toString());
                    writer.newLine();
                }
                if (this.otherOutput) {
                    for (String s : this.errorInfos) {
                        writer.write(s);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void addErrorToken(ErrorToken token) {
        this.errorTokens.add(token);
    }

    public void addErrorInfo(String s) {
        this.errorInfos.add(s);
    }

    public boolean isHaveError() {
        return !this.errorTokens.isEmpty();
    }
}
