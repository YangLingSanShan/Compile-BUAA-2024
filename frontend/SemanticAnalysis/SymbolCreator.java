package frontend.SemanticAnalysis;

import frontend.SemanticAnalysis.Symbols.CharSymbol;
import frontend.SemanticAnalysis.Symbols.FuncSymbol;
import frontend.SemanticAnalysis.Symbols.IntSymbol;
import frontend.SyntaxAnalysis.AstNode;

import java.util.ArrayList;
import java.util.Objects;

public class SymbolCreator {
    public static Symbol createSymbol(AstNode root, Symbol.type symbolType, String temp) {
        String symbolName = root.getChildren().get(0).getToken().getOriginWord();

        if (temp.equals("INTTK") || temp.equals("CHARTK")) {
            ArrayList<?> initValue = new ArrayList<>();  // 使用通配符
            ArrayList<Integer> space = new ArrayList<>();
            AstNode initValAst = null;
            int dim = 0;

            // 处理<ConstExp>和<InitVal>/<ConstInitVal>
            for (AstNode astNode : root.getChildren()) {
                if (astNode.getGrammar().equals("<ConstExp>")) {
                    dim++;
                    space.add(Simplifier.cal(astNode));
                } else if (astNode.getGrammar().equals("<ConstInitVal>") || astNode.getGrammar().equals("<InitVal>")) {
                    initValAst = astNode;
                }
            }

            // 计算初始化值
            if (initValAst != null) {
                if (symbolType == Symbol.type.CONST) {
                    if (temp.equals("INTTK")) {
                        initValue = Simplifier.calConstInitValInt(dim, initValAst);
                    } else {
                        initValue = Simplifier.calConstInitValChar(dim, initValAst);
                    }
                } else if (symbolType == Symbol.type.VAR) {
                    if (temp.equals("INTTK")) {
                        initValue = Simplifier.calConstInitValInt(dim, initValAst);
                    } else {
                        initValue = Simplifier.calConstInitValChar(dim, initValAst);
                    }
                }
            }

            // 根据类型返回符号
            return temp.equals("INTTK")
                    ? new IntSymbol(symbolName, symbolType, dim, (ArrayList<Integer>) initValue, space)
                    : new CharSymbol(symbolName, symbolType, dim, (ArrayList<Character>) initValue, space);
        }

        return null; // 未知类型时返回null
    }

    public static Symbol createConstDefSymbol(AstNode root) {
        String temp = root.getParent().getChildren().get(1).getChildren().get(0).getToken().getReserveWord();
        return createSymbol(root, Symbol.type.CONST, temp);
    }

    public static Symbol createVarDef(AstNode root) {
        String temp = root.getParent().getChildren().get(0).getChildren().get(0).getToken().getReserveWord();
        return createSymbol(root, Symbol.type.VAR, temp);
    }


    public static Symbol createMainFuncDefChecker(AstNode root) {
        String symbolName = root.getChildren().get(1).getToken().getOriginWord();
        return new FuncSymbol(symbolName, Symbol.type.INT);
    }

    public static Symbol createFuncDefChecker(AstNode root) {
        // 获取函数名
        String symbolName = root.getChildren().get(1).getToken().getOriginWord();

        // 获取返回类型的语法标记
        String returnTokenGrammar = root.getChildren().get(0).getChildren().get(0).getGrammar();

        // 使用 switch 表达式来确定返回类型
        Symbol.type returnType = switch (returnTokenGrammar) {
            case "INTTK" -> Symbol.type.INT;
            case "CHARTK" -> Symbol.type.CHAR;
            default -> Symbol.type.VOID;
        };

        // 创建并返回函数符号对象
        return new FuncSymbol(symbolName, returnType);
    }

    public static Symbol createFuncFParam(AstNode root) {
        String symbolName = root.getChildren().get(1).getToken().getOriginWord();
        int dim = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < root.getChildren().size(); i++) {
            if (root.getChildren().get(i).getGrammar().equals("LBRACK")) {
                dim++;
                if (i + 1 < root.getChildren().size() &&
                        root.getChildren().get(i + 1).getGrammar().equals("<ConstExp>")) {
                    list.add(Simplifier.cal(root.getChildren().get(i + 1)));
                } else {
                    list.add(-1);
                }
            }
        }
        String bytType = root.getChildren().get(0).getChildren().get(0).getGrammar();

        return switch (bytType) {
            case "INTTK" -> new IntSymbol(symbolName, Symbol.type.VAR, dim, new ArrayList<>(), list);
            case "CHARTK" -> new CharSymbol(symbolName, Symbol.type.VAR, dim, new ArrayList<>(), list);
            default -> null;
        };
    }
}
