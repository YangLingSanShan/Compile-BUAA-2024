package io;

import frontend.LexerAnalysis.Token;
import io.Conponent.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Settings {
    private static Settings instance;
    private final boolean fileInputSwitch;
    private final boolean errorPrinterSwitch;

    private final boolean lexerPrinterSwitch;

    private final boolean syntaxPrinterSwitch;

    private final boolean semanticPrinterSwitch;

    private final boolean llvmIRSwitch;

    private final boolean assembleSwitch;
    private final boolean isGenerationMode;

    private InputScanner inputScanner;
    private LexerPrinter lexerPrinter;
    private ErrorPrinter errorPrinter;

    private SyntaxPrinter syntaxPrinter;

    private SemanticPrinter semanticPrinter;

    private LLvmIRPrinter lLvmIRPrinter;

    private AssemblePrinter assemblePrinter;

    private final String inputPath;
    private final String errorPath;
    private final String lexerPath;
    private final String syntaxPath;

    private final String semanticPath;

    private final String llvmirPath;

    private final String assemblePath;
    private boolean isOptimize = true;

    private Settings() {
        this.fileInputSwitch = true;
        this.errorPrinterSwitch = true;
        this.lexerPrinterSwitch = true;
        this.syntaxPrinterSwitch = true;
        this.semanticPrinterSwitch = true;
        this.isGenerationMode = true;
        this.llvmIRSwitch = true;
        this.assembleSwitch = true;

        this.inputPath = "testfile.txt";
        this.errorPath = "error.txt";
        this.lexerPath = "lexer.txt";
        this.syntaxPath = "parser.txt";
        this.semanticPath = "symbol.txt";
        this.llvmirPath = "llvm_ir.txt";
        this.assemblePath = "mips.txt";
        try {
            this.setBuffers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void setBuffers() throws IOException {
        this.inputScanner = new InputScanner(this.fileInputSwitch, this.inputPath);
        this.errorPrinter = new ErrorPrinter();
        this.lexerPrinter = new LexerPrinter();
        this.syntaxPrinter = new SyntaxPrinter();
        this.semanticPrinter = new SemanticPrinter();
        this.lLvmIRPrinter = new LLvmIRPrinter();
        this.assemblePrinter = new AssemblePrinter();

        errorPrinter.init(this.errorPath, this.errorPrinterSwitch);
        lexerPrinter.init(this.lexerPath, this.lexerPrinterSwitch);
        syntaxPrinter.init(this.syntaxPath, this.syntaxPrinterSwitch);
        semanticPrinter.init(this.semanticPath, this.semanticPrinterSwitch);
        lLvmIRPrinter.init(this.llvmirPath, this.llvmIRSwitch);
        assemblePrinter.init(this.assemblePath, this.assembleSwitch);
    }

    public void closeBuffers() {
        if (this.errorPrinterSwitch) {
            this.errorPrinter.close();
        }
        if (this.lexerPrinterSwitch) {
            this.lexerPrinter.close();
        }
        if (this.syntaxPrinterSwitch) {
            this.syntaxPrinter.close();
        }
        if (this.semanticPrinterSwitch) {
            this.semanticPrinter.close();
        }
        if (this.llvmIRSwitch) {
            this.lLvmIRPrinter.close();
        }
        if (this.assembleSwitch) {
            this.assemblePrinter.close();
        }
    }

    public BufferedReader getFileInputBuffer() {
        return inputScanner.getReader();
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public void addErrorToken(ErrorToken token) {
        this.errorPrinter.addErrorToken(token);
    }

    public void addNormalToken(ArrayList<Token> tokens) {
        this.lexerPrinter.addToken(tokens);
    }

    public void print() {
        if (this.lexerPrinterSwitch) {
            this.lexerPrinter.print();
        }
        if (this.errorPrinterSwitch) {
            this.errorPrinter.print();
        }
        if (this.syntaxPrinterSwitch) {
            this.syntaxPrinter.print();
        }
        if (this.semanticPrinterSwitch) {
            this.semanticPrinter.print();
        }
        if (this.llvmIRSwitch) {
            this.lLvmIRPrinter.print();
        }
        if (this.assembleSwitch) {
            this.assemblePrinter.print();
        }
        this.closeBuffers();
    }

    public ArrayList<Token> getNormalTokens() {
        return this.lexerPrinter.getTokens();
    }

    public void addErrorInfo(String s) {
        this.errorPrinter.addErrorInfo(s);
    }

    public void addSyntaxLine(String s) {
        this.syntaxPrinter.addLine(s);
    }

    public void addSemanticLine(String s) {
        this.semanticPrinter.addLine(s);
    }

    public boolean isGenerationMode() {
        return this.isGenerationMode;
    }

    public boolean isHaveError() {
        return errorPrinter.isHaveError();
    }

    public void addLLvmIROutput(String string) {
        lLvmIRPrinter.addLLvmIROutput(string);

    }

    public boolean isOptimize() {
        return this.isOptimize;
    }

    public void addAssembleOutput(String string) {
        assemblePrinter.addAssembleOutput(string);
    }

    public void printLLvmIr(String string, String path) {
        try {
            String fileName = new String(path.getBytes(), StandardCharsets.UTF_8);
            Writer writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8, false));
            writer.write(string);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
