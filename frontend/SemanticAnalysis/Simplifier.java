package frontend.SemanticAnalysis;

import frontend.SemanticAnalysis.Symbols.CharSymbol;
import frontend.SemanticAnalysis.Symbols.IntSymbol;
import frontend.SyntaxAnalysis.AstNode;
import io.Conponent.ErrorPrinter;
import io.Settings;

import java.util.ArrayList;

public class Simplifier {
    public static int cal(AstNode root) {
        return switch (root.getGrammar()) {
            case "<Exp>", "<ConstExp>" -> cal(root.getChildren().get(0));
            case "<AddExp>" -> calAddExp(root);
            case "<MulExp>" -> calMulExp(root);
            case "<UnaryExp>" -> calUnaryExp(root);
            case "<PrimaryExp>" -> calPrimaryExp(root);
            case "<Number>" -> calNumber(root);
            case "<Character>" -> calCharacter(root);
            case "<LVal>" -> calLValExp(root);
            default -> 0;
        };
    }

    private static int calLValExp(AstNode root) {
        Symbol symbol = SymbolTable.getSymbol(root.getChildren().get(0).getToken().getOriginWord());
        if (symbol == null) {
            return 0;
        }
        ArrayList<Integer> bracketVal = new ArrayList<>();
        for (int i = 1; i < root.getChildren().size(); i++) {
            if (root.getChildren().get(i).getGrammar().equals("<Exp>")) {
                bracketVal.add(cal(root.getChildren().get(i)));
            }
        }
        if (symbol.getSymbolType() == Symbol.type.VAR || symbol.getSymbolType() == Symbol.type.CONST) {
            if (symbol instanceof IntSymbol intSymbol) {
                return switch (intSymbol.getDim()) {
                    case 0 -> intSymbol.getConstValue();
                    case 1 -> intSymbol.getConstValue(bracketVal.get(0));
                    default -> intSymbol.getConstValue(bracketVal.get(0), bracketVal.get(1));
                };
            } else if (symbol instanceof CharSymbol charSymbol) {
                return switch (charSymbol.getDim()) {
                    case 0 -> charSymbol.getConstValue();
                    case 1 -> charSymbol.getConstValue(bracketVal.get(0));
                    default -> charSymbol.getConstValue(bracketVal.get(0), bracketVal.get(1));
                };
            }
        }
        return 0;
    }

    private static int calPrimaryExp(AstNode root) {
        ArrayList<AstNode> children = root.getChildren();  // 缓存子节点
        AstNode firstChild = children.get(0);  // 缓存第一个子节点

        return switch (firstChild.getGrammar()) {
            case "<Number>", "<Character>", "<LVal>" -> cal(firstChild);  // 处理 Number 或 LVal 或 Character
            default -> cal(children.get(1));  // 处理'(' Exp ')'
        };
    }


    private static int calUnaryExp(AstNode root) {
        ArrayList<AstNode> children = root.getChildren();  // 缓存子节点
        AstNode firstChild = children.get(0);  // 缓存第一个子节点
        int ans = 0;

        if ("<UnaryOp>".equals(firstChild.getGrammar())) {
            ans = switch (firstChild.getChildren().get(0).getGrammar()) {
                case "PLUS" -> cal(children.get(1));  // 处理一元加法
                case "MINU" -> -cal(children.get(1));  // 处理一元减法
                case "NOT" -> cal(children.get(1)) == 0 ? 1 : 0;  // 处理逻辑非运算
                default -> 0;
            };
        } else if ("<PrimaryExp>".equals(firstChild.getGrammar())) {
            ans = calPrimaryExp(firstChild);  // 处理 PrimaryExp
        }

        return ans;
    }

    private static int calMulExp(AstNode root) {
        ArrayList<AstNode> children = root.getChildren();
        int ans = cal(children.get(0));
        for (int i = 1; i < children.size(); i++) {
            switch (children.get(i).getGrammar()) {
                case "MULT" -> ans *= cal(children.get(++i));
                case "DIV" -> {
                    int res = cal(children.get(++i));
                    if (res == 0) {
                        Settings.getInstance().addErrorInfo("DIV / 0");
                    } else {
                        ans /= res;
                    }
                }
                case "MOD" -> { //TODO： throw error
                    int res = cal(children.get(++i));
                    if (res == 0) {
                        Settings.getInstance().addErrorInfo("MOD % 0");
                    } else {
                        ans %= res;
                    }
                }
                default -> ans = 0;
            }
        }
        return ans;
    }

    private static int calAddExp(AstNode root) {
        ArrayList<AstNode> children = root.getChildren();  // 缓存子节点
        int ans = cal(children.get(0));  // 计算第一个节点的值
        // 从第二个子节点开始遍历
        for (int i = 1; i < children.size(); i++) {
            switch (children.get(i).getGrammar()) {
                case "PLUS" -> ans += cal(children.get(++i));
                case "MINU" -> ans -= cal(children.get(++i));
                default -> System.out.println("ERROR CALADDEXP");
            }
        }
        return ans;
    }

    private static int calCharacter(AstNode root) {
        String originWords = root.getChildren().get(0).getToken().getOriginWord();
        char originWord;
        if (originWords.length() == 3) {
            originWord = originWords.charAt(1);
        } else {
            originWord = convertToChar(originWords);
        }
        int asciiValue = originWord;  // 将 char 转换为 ASCII 值
        return asciiValue;
    }

    private static char convertToChar(String string) {
        return switch (string) {
            case "'\\n'" -> '\n';
            case "'\\a'" -> '\u0007';
            case "'\\b'" -> '\b';
            case "'\\t'" -> '\t';
            case "'\\v'" -> '\u000b';
            case "'\\f'" -> '\f';
            case "'\\''" -> '\'';
            case "'\\\"'" -> '\"';
            case "'\\\\'" -> '\\';
            case "'\\0'" -> '\0';
            default -> '\0';
        };
    }

    private static int calNumber(AstNode root) {
        return Integer.parseInt(root.getChildren().get(0).getToken().getOriginWord());
    }

    public static ArrayList<Integer> calConstInitValInt(int dim, AstNode initValAst) {
        ArrayList<Integer> ans = new ArrayList<>();
        // 如果 dim 为 0，直接计算初始值并返回
        if (dim == 0) {
            ans.add(cal(initValAst.getChildren().get(0)));
            return ans;
        }
        // 遍历子节点并递归调用 calConstInitVal
        ArrayList<AstNode> children = initValAst.getChildren(); // 缓存子节点
        for (AstNode child : children) {
            if ("<ConstExp>".equals(child.getGrammar())) {
                ans.addAll(calConstInitValInt(dim - 1, child));
            } else if ("<Exp>".equals(child.getGrammar())) {
                ans.addAll(calConstInitValInt(dim - 1, child));
            }
        }
        return ans;
    }


    public static ArrayList<Character> calConstInitValChar(int dim, AstNode initValAst) {
        ArrayList<Character> ans = new ArrayList<>();
        if (dim == 0) {
            ans.add((char) (cal(initValAst.getChildren().get(0)) & 0xFF));
        }
        ArrayList<AstNode> children = initValAst.getChildren(); // 缓存子节点
        for (AstNode child : children) {
            if ("<ConstExp>".equals(child.getGrammar())) {
                ans.addAll(calConstInitValChar(dim - 1, child));
            } else if ("<Exp>".equals(child.getGrammar())) {
                ans.addAll(calConstInitValChar(dim - 1, child));
            } else if ("STRCON".equals(child.getGrammar())) {
                String toAdd = child.getToken().getOriginWord();    //得到了""123\n""
                if (toAdd.length() > 1 && toAdd.startsWith("\"") && toAdd.endsWith("\"")) {
                    toAdd = toAdd.substring(1, toAdd.length() - 1);     //变为"123\n"
                }
                toAdd = handleEscapeSequences(toAdd);
                // 将字符串转换为字符并添加到 ans 中
                for (char c : toAdd.toCharArray()) {
                    ans.add(c);
                }
                ans.add('\0');
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
