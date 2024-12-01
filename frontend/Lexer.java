package frontend;

import frontend.LexerAnalysis.LexicalAnalyzer;
import frontend.LexerAnalysis.Token;
import io.Settings;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private final HashMap<String, String> reservedWords = new HashMap<>();

    private LexicalAnalyzer analyzer = new LexicalAnalyzer();

    public Lexer() {
        this.reserveWordsInit();
    }

    public void analyze(String line, int lineNum) {
        ArrayList<String> originWords = this.analyzer.analyze(line, lineNum);
        ArrayList<Token> normalTokens = this.parserToToken(originWords, lineNum);
        Settings.getInstance().addNormalToken(normalTokens);
    }


    private ArrayList<Token> parserToToken(ArrayList<String> originWords, int lineNum) {
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        for (String originWord : originWords) {
            if (originWord.matches("'\\\\\\\\'|'\\\\[abfntv0]'|'\\\\x[0-9A-Fa-f]{2}'|'\\\\\\''|'\\\\\\\"'|'.'")) {
                tokenArrayList.add(new Token("CHRCON", originWord, lineNum));
            } else if (reservedWords.containsKey(originWord)) {
                tokenArrayList.add(new Token(reservedWords.get(originWord), originWord, lineNum));
            } else if (originWord.matches("\".*?\"")) {
                tokenArrayList.add(new Token("STRCON", originWord, lineNum));
            } else if (originWord.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                tokenArrayList.add(new Token("IDENFR", originWord, lineNum));
            } else if (originWord.matches("[0-9]+")) {
                tokenArrayList.add(new Token("INTCON", originWord, lineNum));
            }
        }
        return tokenArrayList;
    }

    private void reserveWordsInit() {
        this.reservedWords.put("main", "MAINTK");
        this.reservedWords.put("const", "CONSTTK");
        this.reservedWords.put("int", "INTTK");
        this.reservedWords.put("char", "CHARTK");
        this.reservedWords.put("break", "BREAKTK");
        this.reservedWords.put("continue", "CONTINUETK");
        this.reservedWords.put("if", "IFTK");
        this.reservedWords.put("else", "ELSETK");
        this.reservedWords.put("for", "FORTK");
        this.reservedWords.put("getint", "GETINTTK");
        this.reservedWords.put("getchar", "GETCHARTK");
        this.reservedWords.put("printf", "PRINTFTK");
        this.reservedWords.put("return", "RETURNTK");
        this.reservedWords.put("void", "VOIDTK");
        this.reservedWords.put("+", "PLUS");
        this.reservedWords.put("-", "MINU");
        this.reservedWords.put("*", "MULT");
        this.reservedWords.put("%", "MOD");
        this.reservedWords.put(";", "SEMICN");
        this.reservedWords.put(",", "COMMA");
        this.reservedWords.put("(", "LPARENT");
        this.reservedWords.put(")", "RPARENT");
        this.reservedWords.put("[", "LBRACK");
        this.reservedWords.put("]", "RBRACK");
        this.reservedWords.put("{", "LBRACE");
        this.reservedWords.put("}", "RBRACE");
        this.reservedWords.put("==", "EQL");
        this.reservedWords.put(">=", "GEQ");
        this.reservedWords.put("<=", "LEQ");
        this.reservedWords.put("!=", "NEQ");
        this.reservedWords.put(">", "GRE");
        this.reservedWords.put("<", "LSS");
        this.reservedWords.put("=", "ASSIGN");
        this.reservedWords.put("!", "NOT");
        this.reservedWords.put("&&", "AND");
        this.reservedWords.put("||", "OR");
        this.reservedWords.put("/", "DIV");
    }


}
