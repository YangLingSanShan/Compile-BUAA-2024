package frontend;

import frontend.SemanticAnalysis.Symbol;
import frontend.SemanticAnalysis.SymbolChecker;
import frontend.SemanticAnalysis.SymbolStack;
import frontend.SemanticAnalysis.SymbolTable;
import frontend.SyntaxAnalysis.AstNode;
import io.Settings;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SemanticParser {

    private static SymbolChecker checker = SymbolChecker.getInstance();

    public static void analysis(AstNode root) {
        checker.check(root);
        printSemanticInfo(SymbolTable.getInstance().getScopes());
    }

    private static void printSemanticInfo(HashMap<Integer, SymbolStack> scopes) {
        for (Integer key : scopes.keySet()) {
            SymbolStack stack = scopes.get(key);
            LinkedHashMap<String, Symbol> map = stack.getSymbols();
            for (Symbol symbol : map.values()) {
                if (symbol.getSymbolName().equals("main")) {
                    continue;
                }
                Settings.getInstance().addSemanticLine(key + " " + symbol.getSymbolName() + " " + symbol.getPrintType());
            }

        }
    }

    public static void preTraverse(AstNode root) {
        if (!root.isLeaf()) {
            for (AstNode astNode : root.getChildren()) {
                checker.check(astNode);
                if (astNode.getGrammar().equals("FORTK")) { //考虑是否有for的作用域
                    break;
                }
            }
        }
    }
}
