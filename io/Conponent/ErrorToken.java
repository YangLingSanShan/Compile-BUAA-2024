package io.Conponent;

public class ErrorToken {

    private final char errorType;
    private final int lineNum;

    public ErrorToken(char errorType, int lineNum) {
        this.errorType = errorType;
        this.lineNum = lineNum;
    }

    public char getErrorType() {
        return errorType;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String toString() {
        return this.lineNum + " " + this.errorType;
    }
}
