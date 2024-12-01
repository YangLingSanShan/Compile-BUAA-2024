package frontend.SemanticAnalysis;

import frontend.SemanticAnalysis.Symbols.CharSymbol;
import frontend.SemanticAnalysis.Symbols.FuncSymbol;
import frontend.SemanticAnalysis.Symbols.IntSymbol;
import frontend.SemanticParser;
import frontend.SyntaxAnalysis.AstNode;
import io.Conponent.ErrorToken;
import io.Settings;

import java.util.ArrayList;

public class SymbolChecker {
    private static SymbolChecker instance;
    private SymbolTable table;

    public static SymbolChecker getInstance() {
        if (instance == null) {
            instance = new SymbolChecker();
        }
        return instance;
    }

    public void check(AstNode root) {
        table = SymbolTable.getInstance();
        switch (root.getGrammar()) {
            case "<CompUnit>" -> checkCompUnit(root);
            case "<ConstDef>" -> checkConstDef(root);  // Error b
            case "<VarDef>" -> checkVarDef(root);      // Error b
            case "<Block>" -> checkBlock(root);
            case "FORTK" -> checkForStmt(root);
            case "BREAKTK", "CONTINUETK" -> checkContinueAndBreakStmt(root);    //Error m
            case "RETURNTK" -> checkReturnStmt(root);  // Error f
            case "<LVal>" -> checkLVal(root);          // Error c
            case "<FuncDef>" -> checkFuncDef(root);    // Error b,g
            case "<FuncFParam>" -> checkFuncFParam(root);  // Error b
            case "<MainFuncDef>" -> checkMainFuncDef(root);  // Error b,g
            case "ASSIGN" -> checkAssign(root);        // Error h
            case "<UnaryExp>" -> checkUnaryExp(root);  // Error b,c,d,e
            case "PRINTFTK" -> checkPrintf(root);      // Error l
            default -> SemanticParser.preTraverse(root);
        }
    }

    private void checkPrintf(AstNode root) {
        AstNode rootParent = root.getParent();

        String formatString = rootParent.getChildren().get(2).getToken().getOriginWord();

        // 计算格式化字符串中 %d, %c 的个数
        int argNum = 0;
        for (int i = 0; i < formatString.length() - 1; i++) { // 避免最后一个字符越界
            if (formatString.charAt(i) == '%') {
                char nextChar = formatString.charAt(i + 1);
                if (nextChar == 'd' || nextChar == 'c') {
                    argNum++;
                    i++; // 跳过下一个字符，因为已匹配格式化符号
                }
            }
        }

        // 计算实际传入的表达式数量
        long inputNum = rootParent.getChildren().stream()
                .filter(child -> child.getGrammar().equals("<Exp>"))
                .count();

        // 检查是否存在格式化参数和传入参数数量不匹配的错误
        if (argNum != inputNum) {
            Settings.getInstance().addErrorToken(new ErrorToken('l', root.getEnd()));
        }

        // 继续遍历
        SemanticParser.preTraverse(root);
    }


    private void checkUnaryExp(AstNode root) {
        if (!isFunctionCall(root)) {
            SemanticParser.preTraverse(root);
            return;
        }

        Symbol symbol = SymbolTable.getSymbol(root.getChildren().get(0).getToken().getOriginWord());
        if (symbol == null) {
            Settings.getInstance().addErrorToken(new ErrorToken('c', root.getEnd()));
        } else if (!(symbol instanceof FuncSymbol)) {
            Settings.getInstance().addErrorToken(new ErrorToken('b', root.getEnd()));
        } else {
            checkFunctionCall((FuncSymbol) symbol, root);
        }

        SemanticParser.preTraverse(root);
    }

    private boolean isFunctionCall(AstNode root) {
        return root.getChildren().size() >= 3 &&
                "LPARENT".equals(root.getChildren().get(1).getGrammar());
    }

    private void checkFunctionCall(FuncSymbol funcSymbol, AstNode root) {
        int paramNum = funcSymbol.getParametersNum();
        ArrayList<AstNode> arguments = getArguments(root.getChildren().get(2));

        if (paramNum != arguments.size()) {
            Settings.getInstance().addErrorToken(new ErrorToken('d', root.getStart()));
            return;
        }

        for (int i = 0; i < paramNum; i++) {
            if (!isParameterMatching(funcSymbol, arguments.get(i), i)) {
                Settings.getInstance().addErrorToken(new ErrorToken('e', root.getStart()));
                break;
            }
        }
    }

    private ArrayList<AstNode> getArguments(AstNode node) {
        ArrayList<AstNode> arguments = new ArrayList<>();
        for (AstNode child : node.getChildren()) {
            if ("<Exp>".equals(child.getGrammar())) {
                arguments.add(child);
            }
        }
        return arguments;
    }

    private boolean isParameterMatching(FuncSymbol funcSymbol, AstNode argument, int index) {
        Symbol.type paramType = funcSymbol.getFuncParamTypes().get(index);
        Integer paramDim = funcSymbol.getFuncParamDims().get(index);
        Symbol.type argType = SymbolTypeChecker.getExpType(argument);
        Integer argDim = SymbolDefiner.getExpDim(argument);

        boolean flag = true;
        if (paramDim > 0) {
            if (paramType.equals(Symbol.type.CHAR) && argType.equals(Symbol.type.INT)) {
                flag = !(paramDim == argDim);
            } else if (paramType.equals(Symbol.type.INT) && argType.equals(Symbol.type.CHAR)) {
                flag = !(paramDim == argDim);
            }
        }

        if (argument.getGrammar().equals("<Exp>")) {
            if (!paramType.equals(Symbol.type.VOID)) {
                return true;
            }
        }

        return !paramType.equals(Symbol.type.VOID) &&
                !argType.equals(Symbol.type.VOID) &&
                paramDim.equals(argDim) &&
                flag;
    }


    private void checkAssign(AstNode root) {
        Symbol symbol = null;
        ArrayList<Integer> tempDim = new ArrayList<>();
        AstNode rootAst = root.getParent();

        for (AstNode astNode : rootAst.getChildren()) {
            if (astNode.getGrammar().equals("<LVal>")) {
                for (AstNode child : astNode.getChildren()) {
                    if (child.getGrammar().equals("IDENFR")) {
                        String name = child.getToken().getOriginWord();
                        symbol = SymbolTable.getSymbol(name);
                    } else if (child.getGrammar().equals("<Exp>") && (symbol instanceof IntSymbol || symbol instanceof CharSymbol)) {
                        // 处理数组维度
                        tempDim.add(Simplifier.cal(child));
                    }
                }

                // 检查常量赋值错误
                if (symbol != null && symbol.getSymbolType() == Symbol.type.CONST) {
                    Settings.getInstance().addErrorToken(new ErrorToken('h', root.getEnd()));
                    return;  // 早期返回
                }
            } else if (astNode.getGrammar().equals("<Exp>")) {
                if (symbol instanceof IntSymbol) {
                    int value = Simplifier.cal(astNode);
                    if (tempDim.isEmpty()) {
                        ((IntSymbol) symbol).updateValue(value);
                    } else if (tempDim.size() == 1) {
                        ((IntSymbol) symbol).updateValue(value, tempDim.get(0));
                    } else {
                        ((IntSymbol) symbol).updateValue(value, tempDim.get(0), tempDim.get(1));
                    }
                } else if (symbol instanceof CharSymbol) {
                    char value = (char) (Simplifier.cal(astNode) & 0xFF);
                    if (tempDim.isEmpty()) {
                        ((CharSymbol) symbol).updateValue(value);
                    } else if (tempDim.size() == 1) {
                        ((CharSymbol) symbol).updateValue(value, tempDim.get(0));
                    } else {
                        ((CharSymbol) symbol).updateValue(value, tempDim.get(0), tempDim.get(1));
                    }
                }
            }
        }
        // 预遍历
        SemanticParser.preTraverse(root);
    }


    private void checkMainFuncDef(AstNode root) {
        table.setGlobal(false);
        Symbol symbol = SymbolCreator.createMainFuncDefChecker(root);
        checkFuncDefSubChecker(root, symbol);
    }

    private void checkFuncDef(AstNode root) {
        table.setGlobal(false);
        Symbol symbol = SymbolCreator.createFuncDefChecker(root);
        checkFuncDefSubChecker(root, symbol);
    }

    private void checkFuncDefSubChecker(AstNode root, Symbol symbol) {
        // 尝试添加符号到符号表
        if (!table.addSymbol(symbol)) {
            Settings.getInstance().addErrorToken(new ErrorToken('b', root.getStart()));
        }

        // 设置当前函数符号并推入符号表栈
        SymbolTable.setNowFuncSymbol((FuncSymbol) symbol);
        table.pushStackTable();

        // 遍历函数定义的子节点
        for (AstNode astNode : root.getChildren()) {
            if (astNode.getGrammar().equals("<Block>")) {
                // 定义函数的参数信息
                SymbolDefiner.getParametersInfo(root, (FuncSymbol) symbol);
            }
            SemanticParser.preTraverse(astNode);
        }

        // 弹出符号表栈
        table.exitStack();

        // 获取函数体的最后一个语句块
        AstNode block = root.getChildren().get(root.getChildren().size() - 1);
        int sentenceCount = block.getChildren().size();

        // 如果函数不是void类型，检查是否有有效的返回语句
        if (symbol.getSymbolType() != Symbol.type.VOID && sentenceCount > 1) {
            AstNode lastSentence = block.getChildren().get(sentenceCount - 2);
            if (!isValidReturnStatement(lastSentence)) {
                Settings.getInstance().addErrorToken(new ErrorToken('g', root.getEnd()));
            }
        }
    }

    // 检查是否为有效的返回语句
    private boolean isValidReturnStatement(AstNode lastSentence) {
        if (lastSentence.isLeaf()) {
            return false;
        }

        AstNode returnNode = lastSentence.getChildren().get(0);
        if (!returnNode.getChildren().get(0).getGrammar().equals("RETURNTK")) {
            return false;
        }

        // 检查是否有返回值的表达式
        return returnNode.getChildren().size() >= 2 &&
                returnNode.getChildren().get(1).getGrammar().equals("<Exp>");
    }


    private void checkFuncFParam(AstNode root) {
        Symbol symbol = SymbolCreator.createFuncFParam(root);
        if (!table.addSymbol(symbol)) {
            Settings.getInstance().addErrorToken(new ErrorToken('b', root.getEnd()));
        }
        SemanticParser.preTraverse(root);
    }


    private void checkLVal(AstNode root) {
        if (table.getSymbol(root.getChildren().get(0).getToken().getOriginWord()) == null) {
            Settings.getInstance().addErrorToken(new ErrorToken('c', root.getEnd()));
        }
        SemanticParser.preTraverse(root);
    }

    private void checkReturnStmt(AstNode root) {
        AstNode rootParent = root.getParent();
        if (rootParent.getChildren().size() >= 2 && rootParent.getChildren().get(1).getGrammar().equals("<Exp>")) {
            if (Symbol.type.VOID.equals(table.getCurrentFuncSymbol().getReturnType())) {
                Settings.getInstance().addErrorToken(new ErrorToken('f', rootParent.getEnd()));
            }
        }
        SemanticParser.preTraverse(root);
    }


    private void checkContinueAndBreakStmt(AstNode root) {
        AstNode rootAst = root.getParent();
        if (table.getLoopLevel() <= 0) {
            Settings.getInstance().addErrorToken(new ErrorToken('m', rootAst.getEnd()));
        }
        SemanticParser.preTraverse(root);
    }

    private void checkForStmt(AstNode root) {
        table.enterForStmt();
        AstNode rootAst = root.getParent();
        for (AstNode child : rootAst.getChildren()) {
            if (child.equals(root)) {
                continue;
            }
            check(child);
        }
        table.exitForStmt();
    }

    private void checkBlock(AstNode root) {
        table.pushStackTable();
        SemanticParser.preTraverse(root);
        table.exitStack();
    }

    private void checkVarDef(AstNode root) {
        Symbol symbol = SymbolCreator.createVarDef(root);
        if (!table.addSymbol(symbol)) {
            Settings.getInstance().addErrorToken(new ErrorToken('b', root.getStart()));
        }
        SemanticParser.preTraverse(root);
    }

    public void checkConstDef(AstNode root) {
        Symbol symbol = SymbolCreator.createConstDefSymbol(root);
        if (!table.addSymbol(symbol)) {
            Settings.getInstance().addErrorToken(new ErrorToken('b', root.getStart()));
        }
        SemanticParser.preTraverse(root);
    }

    private void checkCompUnit(AstNode root) {
        table.setGlobal(true);
        table.pushStackTable();
        SemanticParser.preTraverse(root);
        table.exitStack();
    }

}
