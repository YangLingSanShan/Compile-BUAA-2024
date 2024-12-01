package frontend.LexerAnalysis;

public interface ErrorThrower {
    void throwError(char type, int lineNum);
}
