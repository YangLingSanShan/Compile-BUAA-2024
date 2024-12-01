package frontend.SemanticAnalysis;

import frontend.SemanticAnalysis.Symbols.FuncSymbol;
import frontend.SyntaxAnalysis.AstNode;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.LLvmIR.LLvmIRGenerator;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.Value;

import java.util.ArrayList;

public class SymbolDefiner {
    public static void getParametersInfo(AstNode root, FuncSymbol symbol) {
        ArrayList<Symbol.type> funcParamTypes = new ArrayList<>();
        ArrayList<Integer> funcParamDims = new ArrayList<>();

        // 检查是否存在参数列表
        AstNode funcFormalParams = root.getChildren().get(3);
        if (funcFormalParams.getGrammar().equals("<FuncFParams>")) {
            for (AstNode child : funcFormalParams.getChildren()) {
                if (child.getGrammar().equals("<FuncFParam>")) {
                    // 计算参数的维度
                    int dim = (int) child.getChildren().stream()
                            .filter(obj -> obj.getGrammar().equals("LBRACK"))
                            .count();

                    // 获取参数类型
                    String grammar = child.getChildren().get(0).getChildren().get(0).getGrammar();
                    Symbol.type type = switch (grammar) {
                        case "INTTK" -> Symbol.type.INT;
                        case "CHARTK" -> Symbol.type.CHAR;
                        default -> Symbol.type.CONST;
                    };

                    // 添加到参数列表
                    funcParamTypes.add(type);
                    funcParamDims.add(dim);
                }
            }
        }
        // 设置符号的参数信息
        symbol.setParamInfo(funcParamTypes, funcParamDims);
    }

    public static Integer getExpDim(AstNode astNode) {
        return switch (astNode.getGrammar()) {
            case "<Number>", "<Character>" -> 0;
            case "<LVal>" -> getExpDimLVal(astNode);
            case "<UnaryExp>" -> getExpDimUnaryExp(astNode);
            case "<PrimaryExp>" -> getExpDimPrimaryExp(astNode);
            default -> getExpDim(astNode.getChildren().get(0));
        };
    }

    public static Integer getExpDimPrimaryExp(AstNode astNode) {
        if (astNode.getChildren().size() == 1) {
            return getExpDim(astNode.getChildren().get(0));
        } else {
            return getExpDim(astNode.getChildren().get(1));
        }
    }

    public static Integer getExpDimUnaryExp(AstNode astNode) {
        if (astNode.getChildren().size() == 1) {
            return getExpDim(astNode.getChildren().get(0));
        } else {
            return 0;
        }
    }

    public static Integer getExpDimLVal(AstNode astNode) {
        Symbol symbol = SymbolTable.getSymbol(astNode.getChildren().get(0).getToken().getOriginWord());
        int reduceDim = 0;
        for (AstNode child : astNode.getChildren()) {
            if (child.getGrammar().equals("LBRACK")) {
                reduceDim++;
            }
        }
        if (symbol != null && (symbol.getSymbolType() == Symbol.type.VAR || symbol.getSymbolType() == Symbol.type.CONST)) {
            return switch (symbol.getReturnType()) {
                case INT, CHAR -> symbol.getDim() - reduceDim;
                default -> 0;
            };
        }
        return 0;
    }

    public static ArrayList<Value> getLLvmIRConstValues(AstNode rootAst, int dim) {
        ArrayList<Value> ans = new ArrayList<>();
        if (dim == 0) {
            Value value = LLvmIRGenerator.getInstance().llvmIRAnalyse(rootAst.getChildren().get(0));
            ans.add(value);
        } else {
            if (rootAst.getChildren().get(0).getGrammar().equals("STRCON")) {
                String values = handleEscapeSequences(rootAst.getChildren().get(0).getToken().getOriginWord());
                for (int i = 1; i < values.length() - 1; i++) {
                    ans.add(new Constant(new VarType(8), Integer.toString(values.charAt(i))));
                }
                ans.add(new Constant(new VarType(8), "0"));
            } else {
                for (AstNode child : rootAst.getChildren()) {
                    if (child.getGrammar().equals("<Exp>") || child.getGrammar().equals("<ConstExp>")) {
                        ArrayList<Value> temp = getLLvmIRConstValues(child, dim - 1);
                        ans.addAll(temp);
                    }
                }
            }
        }
        return ans;
    }

    private static String handleEscapeSequences(String input) {
        // Replace all escape sequences with their corresponding characters
        return input
                .replace("\\\\", "\\")  // Replace "\\" with "\"
                .replace("\\'", "'")    // Replace "\'" with "'"
                .replace("\\0", "\0")   // Replace "\0" with null character
                .replace("\\n", "\n")   // Replace "\n" with newline
                .replace("\\v", "\u000B") // Replace "\v" with vertical tab
                .replace("\\f", "\u000C") // Replace "\f" with form feed
                .replace("\\\"", "\"")   // Replace "\"" with '"'
                .replace("\\a", "\u0007") // Replace "\a" with bell
                .replace("\\b", "\u0008") // Replace "\b" with backspace
                .replace("\\t", "\t");    // Replace "\t" with tab
    }
}
