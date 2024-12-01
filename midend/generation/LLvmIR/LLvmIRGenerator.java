package midend.generation.LLvmIR;

import frontend.SemanticAnalysis.*;
import frontend.SemanticAnalysis.Symbols.CharSymbol;
import frontend.SemanticAnalysis.Symbols.FuncSymbol;
import frontend.SemanticAnalysis.Symbols.IntSymbol;
import frontend.SyntaxAnalysis.AstNode;
import midend.generation.Values.Instruction.IOInstruction.PutCharInstruction;
import midend.generation.Values.Instruction.IOInstruction.PutIntInstruction;
import midend.generation.Values.Instruction.IOInstruction.PutStrInstruction;
import midend.generation.Values.SubModule.FormatString;
import midend.generation.Generator;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.BasicInstruction.*;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.SubModule.ControlFlow.Loop;
import midend.generation.Values.SubModule.Parameter;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.SubModule.UserPackage.GlobalVar;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.Value;

import java.util.ArrayList;
import java.util.List;

public class LLvmIRGenerator {

    private static LLvmIRGenerator instance;
    private SymbolTable table;

    public static LLvmIRGenerator getInstance() {
        if (instance == null) {
            instance = new LLvmIRGenerator();
        }
        return instance;
    }

    public Value llvmIRAnalyse(AstNode root) {
        table = SymbolTable.getInstance();
        return switch (root.getGrammar()) {
            case "<CompUnit>" -> generateCompUnit(root);

            case "<ConstDef>" -> generateDef("<ConstInitVal>", root);
            case "<VarDef>" -> generateDef("<InitVal>", root);
            case "<Block>" -> generateBlock(root);
//
            case "<Exp>", "<ConstExp>" -> generateExp(root);
            case "<PrimaryExp>" -> generatePrimaryExp(root);
            case "<Number>" -> generateNumber(root);
            case "<Character>" -> generateCharacter(root);
            case "<UnaryExp>" -> generateUnaryExp(root);
            case "<MulExp>" -> generateMulExp(root);
            case "<AddExp>" -> generateAddExp(root);
            case "<RelExp>" -> generateRelExp(root);
            case "<EqExp>" -> generateEqExp(root);
//
            case "IFTK" -> generateIfStmt(root);
            case "FORTK" -> generateForStmt(root);
            case "BREAKTK" -> generateBreakStmt();
            case "CONTINUETK" -> generateContinueStmt();
            case "RETURNTK" -> generateReturnStmt(root);
            case "PRINTFTK" -> generatePrintf(root);
//
            case "<FuncDef>" -> generateFuncDef(root);
            case "<FuncFParam>" -> generateFuncFParam(root);
//
            case "<MainFuncDef>" -> generateMainFuncDef(root);
//
            case "ASSIGN" -> generateAssign(root);
            default -> {
                Generator.preTraverse(root);
                yield null;
            }
        };
    }

    private Value generatePrintf(AstNode root) {
        AstNode rootAst = root.getParent();
        StringBuilder sb = new StringBuilder();
        List<Value> expValueList = rootAst.getChildren().stream()
                .filter(child -> child.getGrammar().equals("<Exp>"))
                .map(this::llvmIRAnalyse).toList();
        String subformatString = rootAst.getChildren().get(2)
                .getToken().getOriginWord().substring(1, rootAst.getChildren().get(2)
                        .getToken().getOriginWord().length() - 1);
        int expCnt = 0;
        for (int i = 0; i < subformatString.length(); i++) {
            if (subformatString.charAt(i) == '%') {
                if (!sb.isEmpty()) {
                    ArrayList<Integer> arrayList = new ArrayList<>();
                    arrayList.add(sb.length() + 1);
                    FormatString str = new FormatString(LLvmIRNameGenerator.getStringLiteralName(), sb.toString(), arrayList);
                    new PutStrInstruction(str);
                    sb.setLength(0);
                }
                Value value = expValueList.get(expCnt++);
                if (value.getType().isInt8()) {
                    value = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", value, new VarType(32));
                }
                if (i + 1 < subformatString.length() && subformatString.charAt(i + 1) == 'c') {
                    new PutCharInstruction(value);
                } else {
                    new PutIntInstruction(value);
                }

                i = i + 1;
            } else if (subformatString.charAt(i) == '\\') {
                sb.append('\n');
                i = i + 1;
            } else {
                sb.append(subformatString.charAt(i));
            }
        }
        if (!sb.isEmpty()) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(sb.length() + 1);
            FormatString str = new FormatString(LLvmIRNameGenerator.getStringLiteralName(),
                    sb.toString(), arrayList);
            new PutStrInstruction(str);
        }
        return null;
    }

    private Value generateReturnStmt(AstNode root) {
        AstNode rootAst = root.getParent();
        LLvmIRType returnType = LLvmIRNameGenerator.getNowFunc().getReturnType();
        Value retValue = null;

        // 检查是否存在返回值表达式
        if (rootAst.getChildren().size() > 1 && rootAst.getChildren().get(1).getGrammar().equals("<Exp>")) {
            retValue = llvmIRAnalyse(rootAst.getChildren().get(1));
            if (returnType.isInt32() && retValue.getType().isInt8()) {
                return new RetInstruction(new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", retValue, new VarType(32)));
            } else if (returnType.isInt8() && retValue.getType().isInt32()) {
                return new RetInstruction(new TruncInstruction(LLvmIRNameGenerator.getLocalVarName(), "trunc", retValue, new VarType(8)));
            } else {
                return new RetInstruction(retValue);
            }
        } else if (returnType.isInt32()) {
            return new RetInstruction(new Constant(new VarType(32), "0"));
        } else if (returnType.isInt8()) {
            return new RetInstruction(new Constant(new VarType(8), "0"));
        } else {
            return new RetInstruction(null);
        }
    }

    private Value generateContinueStmt() {
        // 获取当前循环
        Loop currentLoop = LLvmIRNameGenerator.getNowLoop();

        // 如果存在递增部分，进行分析
        if (currentLoop.getForStmtVal2() != null) {
            llvmIRAnalyse(currentLoop.getForStmtVal2());
        }

        // 跳转到条件块或当前循环块
        BasicBlock targetBlock = (currentLoop.getCondBlock() != null) ?
                currentLoop.getCondBlock() :
                currentLoop.getCurrentLoopBlock();

        new JumpInstruction(targetBlock);

        return null;
    }

    private Value generateBreakStmt() {
        new JumpInstruction(LLvmIRNameGenerator.getNowLoop().getFollowBlock());
        return null;
    }

    private Value generateForStmt(AstNode root) {
        AstNode rootAst = root.getParent();
        AstNode initializationNode = null;
        AstNode conditionNode = null;
        AstNode incrementNode = null;

        // 识别 for 语句的组成部分
        for (int i = 0; i < rootAst.getChildren().size(); i++) {
            if (rootAst.getChildren().get(i).getGrammar().equals("<ForStmt>")) {
                if (i == 2) {
                    initializationNode = rootAst.getChildren().get(i);
                } else {
                    incrementNode = rootAst.getChildren().get(i);
                }
            } else if (rootAst.getChildren().get(i).getGrammar().equals("<Cond>")) {
                conditionNode = rootAst.getChildren().get(i);
            }
        }

        // 进入循环上下文
        table.enterForStmt();

        // 分析初始化部分
        if (initializationNode != null) {
            llvmIRAnalyse(initializationNode);
        }

        BasicBlock conditionBlock = null;
        if (conditionNode != null) {
            conditionBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
        }
        BasicBlock currentLoopBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
        BasicBlock followBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());

        // 管理循环结构
        LLvmIRNameGenerator.pushLoop(new Loop(conditionBlock, incrementNode, currentLoopBlock, followBlock));

        // 根据条件存在与否创建跳转
        if (conditionNode != null) {
            new JumpInstruction(conditionBlock);
            LLvmIRNameGenerator.setNowBasicBlock(conditionBlock);
            LLvmIRDefiner.getInstance().createCondIr(conditionNode, currentLoopBlock, followBlock);
        } else {
            new JumpInstruction(currentLoopBlock);
        }

        LLvmIRNameGenerator.setNowBasicBlock(currentLoopBlock);
        llvmIRAnalyse(rootAst.getChildren().get(rootAst.getChildren().size() - 1)); // 分析循环体

        // 分析递增部分
        if (incrementNode != null) {
            llvmIRAnalyse(incrementNode);
        }

        // 跳转回条件检查或循环
        if (conditionNode != null) {
            new JumpInstruction(conditionBlock);
        } else {
            new JumpInstruction(currentLoopBlock);
        }

        // 退出循环上下文
        LLvmIRNameGenerator.setNowBasicBlock(followBlock);
        LLvmIRNameGenerator.popLoop();
        table.exitForStmt();
        return null;
    }


    private Value generateIfStmt(AstNode root) {
        AstNode rootAst = root.getParent();
        BasicBlock thenBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
        BasicBlock followBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
        BasicBlock elseBlock = null;

        boolean hasElse = rootAst.getChildren().size() > 5;

        if (hasElse) {
            elseBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
            LLvmIRDefiner.getInstance().createCondIr(rootAst.getChildren().get(2), thenBlock, elseBlock);
        } else {
            LLvmIRDefiner.getInstance().createCondIr(rootAst.getChildren().get(2), thenBlock, followBlock);
        }

        LLvmIRNameGenerator.setNowBasicBlock(thenBlock);
        llvmIRAnalyse(rootAst.getChildren().get(4));
        new JumpInstruction(followBlock);

        if (hasElse) {
            LLvmIRNameGenerator.setNowBasicBlock(elseBlock);
            llvmIRAnalyse(rootAst.getChildren().get(6));
            new JumpInstruction(followBlock);
        }

        LLvmIRNameGenerator.setNowBasicBlock(followBlock);
        return null;
    }


    private Value generateFuncFParam(AstNode root) {
        Symbol symbol = SymbolCreator.createFuncFParam(root);
        table.addSymbol(symbol);

        LLvmIRType type = determineParamType(symbol);
        Parameter param = new Parameter(type, LLvmIRNameGenerator.getParameterName());

        if (param.getType().isInt32()) {
            Instruction allocaInstr = new AllocaInstruction(param.getType(), LLvmIRNameGenerator.getLocalVarName());
            symbol.setValue(allocaInstr);
            new StoreInstruction(param, allocaInstr);
        } else if (param.getType().isInt8()) {
            Instruction allocaInstr = new AllocaInstruction(param.getType(), LLvmIRNameGenerator.getLocalVarName());
            symbol.setValue(allocaInstr);
            new StoreInstruction(param, allocaInstr);
        } else {
            symbol.setValue(param);
        }

        Generator.preTraverse(root);
        return null;
    }

    private LLvmIRType determineParamType(Symbol symbol) {
        if (symbol instanceof CharSymbol) {
            return symbol.getDim() == 0 ? new VarType(8) : new PointerType(new VarType(8));
        } else if (symbol instanceof IntSymbol) {
            return symbol.getDim() == 0 ? new VarType(32) : new PointerType(new VarType(32));
        }
        return null; // 未知
    }


    private Value generateFuncDef(AstNode root) {
        table.setGlobal(false);
        FuncSymbol funcSymbol = (FuncSymbol) SymbolCreator.createFuncDefChecker(root);
        return generateFuncDefSub(root, funcSymbol);
    }

    private Value generateMainFuncDef(AstNode root) {
        table.setGlobal(false);
        FuncSymbol funcSymbol = (FuncSymbol) SymbolCreator.createMainFuncDefChecker(root);
        return generateFuncDefSub(root, funcSymbol);
    }

    private Value generateFuncDefSub(AstNode root, FuncSymbol funcSymbol) {
        // 添加函数符号并设置为当前符号
        table.addSymbol(funcSymbol);
        table.setNowFuncSymbol(funcSymbol);
        table.pushStackTable();

        // 确定返回类型
        LLvmIRType returnType = switch (funcSymbol.getReturnType()) {
            case INT -> new VarType(32);
            case CHAR -> new VarType(8);
            default -> new VarType(0);
        };

        // 创建函数并设置当前函数
        Function function = new Function(LLvmIRNameGenerator.getFuncName(funcSymbol.getSymbolName()), returnType);
        funcSymbol.setFunction(function);
        LLvmIRNameGenerator.setNowFunction(function);

        // 创建基本块并设置为当前基本块
        BasicBlock basicBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
        LLvmIRNameGenerator.setNowBasicBlock(basicBlock);

        // 预遍历 AST
        Generator.preTraverse(root);

        // 检查当前基本块并添加返回指令
        BasicBlock preBlock = LLvmIRNameGenerator.getNowBasicBlock();
        if (preBlock.isEmpty() || !(preBlock.getPreInstruction() instanceof RetInstruction)) {
            Value returnValue = returnType.isInt32() ? new Constant(new VarType(32), "0") : null;
            new RetInstruction(returnValue);
        }

        table.exitStack();
        return null;
    }


    private Value generateAssign(AstNode root) {
        AstNode father = root.getParent();
        if (father.getChildren().size() > 2 && father.getChildren().get(2).getGrammar().equals("GETINTTK")) {
            return LLvmIRDefiner.getInstance().createGetInt(father);
        } else if (father.getChildren().size() > 2 && father.getChildren().get(2).getGrammar().equals("GETCHARTK")) {
            return LLvmIRDefiner.getInstance().createGetChar(father);
        } else {
            Value lval = LLvmIRDefiner.getInstance().createAssignIR(father.getChildren().get(0));
            Value exp = llvmIRAnalyse(father.getChildren().get(2));
            if (exp.getType().isInt8() && lval.getType().isIntPtr32()) {
                exp = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", exp, new VarType(32));
            } else if (exp.getType().isInt32() && lval.getType().isIntPtr8()) {
                exp = new TruncInstruction(LLvmIRNameGenerator.getLocalVarName(), "trunc", exp, new VarType(8));
            }
            return new StoreInstruction(exp, lval);
        }
    }

    private Value generateEqExp(AstNode root) {
        Value ans = llvmIRAnalyse(root.getChildren().get(0));

        for (int i = 1; i < root.getChildren().size(); i++) {
            AstNode child = root.getChildren().get(i);
            if (child.getGrammar().matches("EQL|NEQ")) {
                ans = ensureInt32Type(ans);
                Value res = ensureInt32Type(llvmIRAnalyse(root.getChildren().get(i + 1)));

                String cmpType = child.getGrammar().equals("EQL") ? "eq" : "ne";
                ans = new IcmpInstruction(LLvmIRNameGenerator.getLocalVarName(), cmpType, ans, res);
            }
        }
        return ans;
    }

    // 辅助方法：确保值类型为 Int32


    private Value generateRelExp(AstNode root) {
        Value ans = llvmIRAnalyse(root.getChildren().get(0));

        for (int i = 1; i < root.getChildren().size(); i++) {
            // 确保 ans 为 Int32 类型
            ans = ensureInt32Type(ans);

            // 分析右操作数并确保其类型为 Int32
            Value res = llvmIRAnalyse(root.getChildren().get(i + 1));
            if (!res.getType().isInt32()) {
                res = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", res, new VarType(32));
            }

            // 根据操作符构建对应的 IcmpInstruction
            String operation = switch (root.getChildren().get(i).getGrammar()) {
                case "LSS" -> "slt";
                case "LEQ" -> "sle";
                case "GRE" -> "sgt";
                case "GEQ" -> "sge";
                default -> null;
            };

            if (operation != null) {
                ans = new IcmpInstruction(LLvmIRNameGenerator.getLocalVarName(), operation, ans, res);
            }
            i++;
        }

        return ans;
    }

    private Value ensureInt32Type(Value value) {
        if (!value.getType().isInt32()) {
            return new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", value, new VarType(32));
        }
        return value;
    }

    private Value generateAddExp(AstNode root) {
        Value ans = llvmIRAnalyse(root.getChildren().get(0));
        for (int i = 1; i < root.getChildren().size(); i++) {
            String operator = switch (root.getChildren().get(i).getGrammar()) {
                case "PLUS" -> "add";
                case "MINU" -> "sub";
                default -> "???";
            };
            Value operand2 = llvmIRAnalyse(root.getChildren().get(++i));
            if (operand2.getType().isInt8()) {
                operand2 = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", operand2, new VarType(32));
            }
            if (ans.getType().isInt8()) {
                ans = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", ans, new VarType(32));
            }
            ans = new CalcInstruction(LLvmIRNameGenerator.getLocalVarName(), operator, ans, operand2);
        }
        return ans;
    }

    private Value generateMulExp(AstNode root) {
        Value ans = llvmIRAnalyse(root.getChildren().get(0));
        for (int i = 1; i < root.getChildren().size(); i++) {
            String operator = switch (root.getChildren().get(i).getGrammar()) {
                case "MULT" -> "mul";
                case "DIV" -> "sdiv";
                case "MOD" -> "srem";
                default -> "???";
            };
            Value operand2 = llvmIRAnalyse(root.getChildren().get(++i));
            if (operand2.getType().isInt8()) {
                operand2 = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", operand2, new VarType(32));
            }
            if (ans.getType().isInt8()) {
                ans = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", ans, new VarType(32));
            }
            ans = new CalcInstruction(LLvmIRNameGenerator.getLocalVarName(), operator, ans, operand2);
        }
        return ans;
    }

    private Value generateUnaryExp(AstNode root) {
        // 获取第一个子节点及其文法
        AstNode firstChild = root.getChildren().get(0);
        String grammar = firstChild.getGrammar();

        // 如果是 <PrimaryExp>，直接返回结果
        if ("<PrimaryExp>".equals(grammar)) {
            return llvmIRAnalyse(firstChild);
        }

        // 如果是 <UnaryOp>，根据操作符类型处理
        if ("<UnaryOp>".equals(grammar)) {
            Value ans = llvmIRAnalyse(root.getChildren().get(1));
            String unaryOp = firstChild.getChildren().get(0).getGrammar();
            if (ans.getType().isInt8()) {
                ans = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", ans, new VarType(32));
            }
            switch (unaryOp) {
                case "PLUS":
                    return ans;
                case "MINU":
                    return new CalcInstruction(
                            LLvmIRNameGenerator.getLocalVarName(),
                            "sub",
                            new Constant(new VarType(32), "0"),
                            ans
                    );
                default:
                    Value icmpResult = new IcmpInstruction(
                            LLvmIRNameGenerator.getLocalVarName(),
                            "eq",
                            new Constant(new VarType(32), "0"),
                            ans
                    );
                    return new ZextInstruction(
                            LLvmIRNameGenerator.getLocalVarName(),
                            "zext",
                            icmpResult,
                            new VarType(32)
                    );
            }
        }

        // 处理函数调用
        FuncSymbol funcSymbol = (FuncSymbol) SymbolTable.getSymbol(firstChild.getToken().getOriginWord());
        if (funcSymbol != null) {
            Function function = funcSymbol.getfunction();
            ArrayList<Value> params = new ArrayList<>();

            // 处理函数参数
            if (root.getChildren().size() > 2 && "<FuncRParams>".equals(root.getChildren().get(2).getGrammar())) {
                for (AstNode child : root.getChildren().get(2).getChildren()) {
                    if ("<Exp>".equals(child.getGrammar())) {
                        params.add(llvmIRAnalyse(child));
                    }
                }
            }
            return new CallInstruction(LLvmIRNameGenerator.getLocalVarName(), function, params);
        }
        return llvmIRAnalyse(firstChild);
    }


    private Value generateCharacter(AstNode root) {
        String originWord = root.getChildren().get(0).getToken().getOriginWord();
        originWord = handleEscapeSequences(originWord);
        int asciiValue = originWord.charAt(1);
        return new Constant(new VarType(8), Integer.toString(asciiValue));
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

    private Value generateNumber(AstNode root) {
        return new Constant(new VarType(32), root.getChildren().get(0).getToken().getOriginWord());
    }

    private Value generateLVal(AstNode root) {
        return LLvmIRDefiner.getInstance().createLValIR(root);
    }

    private Value generatePrimaryExp(AstNode root) {
        AstNode child = root.getChildren().get(0);
        return switch (child.getGrammar()) {
            case "LPARENT" -> generateExp(root.getChildren().get(1));
            case "<LVal>" -> generateLVal(child);
            case "<Character>" -> generateCharacter(child);
            case "<Number>" -> generateNumber(child);
            default -> null;
        };
    }


    private Value generateExp(AstNode root) {
        return llvmIRAnalyse(root.getChildren().get(0));
    }

    private Value generateBlock(AstNode root) {
        table.pushStackTable();
        Generator.preTraverse(root);
        table.exitStack();
        return null;
    }

    private Value generateDef(String type, AstNode root) {
        Symbol symbol;
        if (type.equals("<ConstInitVal>")) {
            symbol = SymbolCreator.createConstDefSymbol(root);
        } else {
            symbol = SymbolCreator.createVarDef(root);
        }

        table.addSymbol(symbol);

        // 全局常量处理
        if (symbol.getSymbolLevel() == 0) {
            GlobalVar globalVar = new GlobalVar(
                    new PointerType(symbol.getInitial().getType()),
                    LLvmIRNameGenerator.getGlobalVarName(symbol.getSymbolName()),
                    symbol.getInitial()
            );
            symbol.setValue(globalVar);
            return null;
        }

        // 局部处理
        Instruction instruction = allocateLocalConst(symbol);
        symbol.setValue(instruction);

        // 检查并初始化
        if (type.equals(root.getChildren().get(root.getChildren().size() - 1).getGrammar())) {
            if (symbol.getDim() == 0) {
                Value value = SymbolDefiner.getLLvmIRConstValues(
                        root.getChildren().get(root.getChildren().size() - 1), 0).get(0);
                if (value.getType().isInt8() && symbol instanceof IntSymbol) {
                    value = new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", value, new VarType(32));
                } else if (value.getType().isInt32() && symbol instanceof CharSymbol) {
                    value = new TruncInstruction(LLvmIRNameGenerator.getLocalVarName(), "trunc", value, new VarType(8));
                }
                new StoreInstruction(value, instruction);
            } else {
                initializeArrayConst(symbol, root, instruction);
            }
        }
        return null;
    }

    // 抽取局部常量分配的方法
    private Instruction allocateLocalConst(Symbol symbol) {
        VarType varType = new VarType(symbol instanceof IntSymbol ? 32 : 8);
        if (symbol.getDim() > 0) {
            return new AllocaInstruction(new ArrayType(symbol.getSpace(), varType), LLvmIRNameGenerator.getLocalVarName());
        } else {
            return new AllocaInstruction(varType, LLvmIRNameGenerator.getLocalVarName());
        }
    }

    // 抽取数组常量初始化的方法
    private void initializeArrayConst(Symbol constSymbol, AstNode root, Instruction instruction) {
        Value pointer = instruction;
        ArrayList<Value> valueList = SymbolDefiner.getLLvmIRConstValues(
                root.getChildren().get(root.getChildren().size() - 1), constSymbol.getDim()
        );
        int offset = 0;
        for (Value value : valueList) {
            Instruction elementInstruction = new GetEleInstruction(
                    LLvmIRNameGenerator.getLocalVarName(), pointer,
                    new Constant(value.getType(), String.valueOf(offset))
            );
            if (value.getType().isInt8() && elementInstruction.getType().isIntPtr32()) {
                new ZextInstruction(LLvmIRNameGenerator.getLocalVarName(), "zext", value, new VarType(32));
            } else if (value.getType().isInt32() && elementInstruction.getType().isIntPtr8()) {
                new TruncInstruction(LLvmIRNameGenerator.getLocalVarName(), "trunc", value, new VarType(8));
            }
            new StoreInstruction(value, elementInstruction);
            offset++;
        }
    }

    private Value generateCompUnit(AstNode root) {
        table.setGlobal(true);
        table.pushStackTable();
        Generator.preTraverse(root);
        table.exitStack();
        return null;
    }

}
