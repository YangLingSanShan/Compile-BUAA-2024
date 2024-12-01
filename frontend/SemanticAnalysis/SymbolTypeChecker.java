package frontend.SemanticAnalysis;

import frontend.SemanticAnalysis.Symbols.CharSymbol;
import frontend.SemanticAnalysis.Symbols.IntSymbol;
import frontend.SyntaxAnalysis.AstNode;

public class SymbolTypeChecker {
    public static Symbol.type getExpType(AstNode astNode) {
        return switch (astNode.getGrammar()) {
            case "<Number>" -> getExpTypeNumber(astNode);
            case "<Character>" -> getExpTypeCharacter(astNode);
            case "<LVal>" -> getExpTypeLVal(astNode);
            case "<UnaryExp>" -> getExpTypeUnaryExp(astNode);
            case "<PrimaryExp>" -> getExpTypePrimaryExp(astNode);
            default -> getExpTypeDefault(astNode);
        };
    }

    private static Symbol.type getExpTypeCharacter(AstNode astNode) {
        return isCharCon(astNode) ? Symbol.type.CHAR : Symbol.type.VOID;
    }

    private static boolean isCharCon(AstNode astNode) {
        return "CHARCON".equals(astNode.getChildren().get(0).getGrammar());
    }


    private static Symbol.type getExpTypeLVal(AstNode node) {
        Symbol symbol = SymbolTable.getSymbol(getTokenOriginWord(node));
        if (symbol instanceof IntSymbol) {
            return Symbol.type.INT;
        } else if (symbol instanceof CharSymbol) {
            return Symbol.type.CHAR;
        } else {
            return Symbol.type.VOID;
        }
    }

    private static Symbol.type getExpTypeNumber(AstNode astNode) {
        return isIntCon(astNode) ? Symbol.type.INT : Symbol.type.VOID;
    }

    private static Symbol.type getExpTypeUnaryExp(AstNode astNode) {
        if (astNode.getChildren().size() == 1) {
            return getExpType(astNode.getChildren().get(0));
        }
        return switch (astNode.getChildren().get(0).getGrammar()) {
            case "IDENFR" -> getReturnType(astNode);
            case "<UnaryOp>" -> getExpType(astNode.getChildren().get(1));
            default -> Symbol.type.VAR;
        };
    }

    private static Symbol.type getExpTypePrimaryExp(AstNode astNode) {
        return getExpType(astNode.getChildren().size() == 1 ?
                astNode.getChildren().get(0) :
                astNode.getChildren().get(1));
    }

    private static Symbol.type getExpTypeDefault(AstNode astNode) {
        return getExpType(astNode.getChildren().get(0));
    }

    private static boolean isIntCon(AstNode astNode) {
        return "INTCON".equals(astNode.getChildren().get(0).getGrammar());
    }

    private static String getTokenOriginWord(AstNode astNode) {
        return astNode.getChildren().get(0).getToken().getOriginWord();
    }

    private static Symbol.type getReturnType(AstNode astNode) {
        Symbol symbol = SymbolTable.getSymbol(getTokenOriginWord(astNode));
        return symbol != null ? symbol.getReturnType() : Symbol.type.VOID;
    }
}
