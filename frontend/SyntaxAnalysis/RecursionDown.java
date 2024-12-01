package frontend.SyntaxAnalysis;

import frontend.LexerAnalysis.Token;
import frontend.SyntaxAnalysis.Units.Decl;
import frontend.SyntaxAnalysis.Units.FuncDef;
import frontend.SyntaxAnalysis.Units.MainFuncDef;
import io.Settings;

import java.util.ArrayList;

public class RecursionDown {
    private static Token preToken;
    private static AstNode preAst;

    private static int prePosition;

    public RecursionDown() {
        prePosition = 0;
        preToken = Settings.getInstance().getNormalTokens().get(prePosition);
    }

    public static Token getPreToken() {
        return preToken;
    }

    public static void setPreAst(AstNode preAst) {
        RecursionDown.preAst = preAst;
    }

    public static AstNode getPreAstNode() {
        return preAst;
    }

    public static Token getPositionToken(int position) {
        ArrayList<Token> tokens = Settings.getInstance().getNormalTokens();
        if (!(prePosition + position > tokens.size() - 1 || prePosition + position < 0)) {
            return tokens.get(prePosition + position);
        }
        return null;
    }

    public static void nextToken() {
        ArrayList<Token> tokens = Settings.getInstance().getNormalTokens();
        if (prePosition == tokens.size() - 1) {
            return;
        }
        prePosition++;
        preToken = tokens.get(prePosition);
    }

    public void beginRecursionDown(AstNode root) {
        //TODO: 全局变量定义
        while (Checker.isDecl()) {
            new Decl(root);
        }
        //TODO: 函数定义
        while (Checker.isFunc()) {
            new FuncDef(root);
        }
        //TODO: main函数定义
        new MainFuncDef(root);
    }
}
