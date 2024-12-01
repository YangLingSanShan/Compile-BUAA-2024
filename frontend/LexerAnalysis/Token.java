package frontend.LexerAnalysis;

public class Token {
    private final String reserveWord;
    private final String originWord;

    private final int lineNumber;
    public Token(String reserveWord, String originWord, int lineNumber) {
        this.reserveWord = reserveWord;
        this.originWord = originWord;
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getOriginWord() {
        return originWord;
    }

    public String getReserveWord() {
        return reserveWord;
    }

    public String toString() {
        return this.reserveWord + " " + this.originWord;
    }
}
