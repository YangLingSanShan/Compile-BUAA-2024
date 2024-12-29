package midend.generation.LLvmIR;

import java.util.ArrayList;

import frontend.SemanticAnalysis.Symbol;
import frontend.SyntaxAnalysis.AstNode;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.BasicInstruction.*;
import midend.generation.Values.Instruction.IOInstruction.GetCharInstruction;
import midend.generation.Values.Instruction.IOInstruction.GetIntInstruction;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.Value;
import frontend.SemanticAnalysis.SymbolTable;

public class LLvmIRDefiner {

    private static LLvmIRDefiner instance;

    public static LLvmIRDefiner getInstance() {
        if (instance == null) {
            instance = new LLvmIRDefiner();
        }
        return instance;
    }

    public void createCondIr(AstNode root, BasicBlock thenBlock, BasicBlock elseBlock) {
        if ("<LOrExp>".equals(root.getChildren().get(0).getGrammar())) {
            createOrIr(root.getChildren().get(0), thenBlock, elseBlock);
        }
    }

    public void createOrIr(AstNode child, BasicBlock thenBlock, BasicBlock elseBlock) {
        for (AstNode node : child.getChildren()) {
            switch (node.getGrammar()) {
                case "<LAndExp>":
                    handleAndExp(child, node, thenBlock, elseBlock);
                    break;
                case "<LOrExp>":
                    createOrIr(node, thenBlock, elseBlock);
                    break;
            }
        }
    }

    private void handleAndExp(AstNode child, AstNode node, BasicBlock thenBlock, BasicBlock elseBlock) {
        boolean isInOrExp = child.getParent().getChildren().size() > 1 || child.getChildren().size() > 1;
        boolean isParentOrExp = child.getParent().getGrammar().equals("<LOrExp>");

        if (isInOrExp && isParentOrExp) {
            BasicBlock nextBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
            createAndIr(node, thenBlock, nextBlock);
            LLvmIRNameGenerator.setNowBasicBlock(nextBlock);
        } else {
            createAndIr(node, thenBlock, elseBlock);
        }
    }

    public void createAndIr(AstNode child, BasicBlock thenBlock, BasicBlock elseBlock) {
        for (AstNode node : child.getChildren()) {
            if (node.getGrammar().equals("<EqExp>")) {
                if ((child.getParent().getChildren().size() > 1 || child.getChildren().size() > 1)
                        && child.getParent().getGrammar().equals("<LAndExp>")) {
                    BasicBlock nextBlock = new BasicBlock(LLvmIRNameGenerator.getBlockName());
                    createEqIr(node, nextBlock, elseBlock);
                    LLvmIRNameGenerator.setNowBasicBlock(nextBlock);
                } else {
                    createEqIr(node, thenBlock, elseBlock);
                }
            } else if (node.getGrammar().equals("<LAndExp>")) {
                createAndIr(node, thenBlock, elseBlock);
            }
        }
    }

    /**
     * EqExp -> RelExp | EqExp ('=='|'!=') RelExp
     */
    public void createEqIr(AstNode node, BasicBlock thenBlock, BasicBlock elseBlock) {
        Value cond = LLvmIRGenerator.getInstance().llvmIRAnalyse(node);
        if (cond.getType().isInt32() || cond.getType().isInt8()) {
            cond = new IcmpInstruction(LLvmIRNameGenerator.getLocalVarName(), "ne", cond, new Constant(new VarType(32), "0"));
        }
        new BrInstruction(cond, thenBlock, elseBlock);
    }

    public Value createGetInt(AstNode root) {
        Value pointer = createAssignIR(root.getChildren().get(0));
        GetIntInstruction getIntDeclare = new GetIntInstruction(LLvmIRNameGenerator.getLocalVarName(), "call");
        return new StoreInstruction(getIntDeclare, pointer);
    }

    public Value createGetChar(AstNode root) {
        Value pointer = createAssignIR(root.getChildren().get(0));
        GetCharInstruction getCharInstruction = new GetCharInstruction(LLvmIRNameGenerator.getLocalVarName(), "call");
        TruncInstruction truncInstruction = new TruncInstruction(LLvmIRNameGenerator.getLocalVarName(), "trunc", getCharInstruction, new VarType(8));
        return new StoreInstruction(truncInstruction, pointer);
    }

    public Value createAssignIR(AstNode root) {
        ArrayList<Value> values = new ArrayList<>();

        for (AstNode child : root.getChildren()) {
            if ("<Exp>".equals(child.getGrammar())) {
                Value value = LLvmIRGenerator.getInstance().llvmIRAnalyse(child);
                if (value instanceof CallInstruction) {
                    value = ((CallInstruction) value).getBelongBasicBlock().removeAndReturnInstruction();
                }
                values.add(value);

            }
        }

        String originWord = root.getChildren().get(0).getToken().getOriginWord();
        Symbol intSymbol = SymbolTable.getSymbol(originWord);

        if (intSymbol == null) {
            return null;
        }

        Integer dim = intSymbol.getDim();
        ArrayList<Integer> space = intSymbol.getSpace();

        switch (dim) {
            case 0:
                return intSymbol.getValue();
            case 1:
                return new GetEleInstruction(LLvmIRNameGenerator.getLocalVarName(), intSymbol.getValue(), values.get(0));
            default:
                Value multiplier = new CalcInstruction(LLvmIRNameGenerator.getLocalVarName(), "mul", new Constant(new VarType(32), String.valueOf(space.get(1))), values.get(0));
                Value index = new CalcInstruction(LLvmIRNameGenerator.getLocalVarName(), "add", multiplier, values.get(1));
                return new GetEleInstruction(LLvmIRNameGenerator.getLocalVarName(), intSymbol.getValue(), index);
        }
    }


    public Value createLValIR(AstNode root) {
        int expSum = 0;
        ArrayList<Value> values = new ArrayList<>();
        for (AstNode child : root.getChildren()) {
            if ("<Exp>".equals(child.getGrammar())) {
                values.add(LLvmIRGenerator.getInstance().llvmIRAnalyse(child));
                expSum++;
            }
        }
        Symbol symbol = SymbolTable.getSymbol(root.getChildren().get(0).getToken().getOriginWord());
        return createSymbolValueIR(values, expSum, symbol);
    }

    public Value createSymbolValueIR(ArrayList<Value> values, int expNum, Symbol symbol) {
        int dim = symbol.getDim();
        Value value = symbol.getValue();
        ArrayList<Integer> space = symbol.getSpace();
        if (dim == 0) {
            return new LoadInstruction(LLvmIRNameGenerator.getLocalVarName(), value);
        } else if (dim == 1) {
            return (expNum == 0) ?
                    new GetEleInstruction(LLvmIRNameGenerator.getLocalVarName(), value, new Constant(new VarType(32), "0")) :
                    new LoadInstruction(LLvmIRNameGenerator.getLocalVarName(), new GetEleInstruction(LLvmIRNameGenerator.getLocalVarName(), value, values.get(0)));
        } else {
            if (expNum == 0) {
                return new GetEleInstruction(LLvmIRNameGenerator.getLocalVarName(), value, new Constant(new VarType(32), "0"));
            } else if (expNum == 1) {
                Instruction instr = new CalcInstruction(LLvmIRNameGenerator.getLocalVarName(), "mul", new Constant(new VarType(32), String.valueOf(space.get(1))), values.get(0));
                return new GetEleInstruction(LLvmIRNameGenerator.getLocalVarName(), value, instr);
            } else {
                Instruction instr = new CalcInstruction(LLvmIRNameGenerator.getLocalVarName(), "add", new CalcInstruction(LLvmIRNameGenerator.getLocalVarName(), "mul", new Constant(new VarType(32), String.valueOf(space.get(1))), values.get(0)), values.get(1));
                return new LoadInstruction(LLvmIRNameGenerator.getLocalVarName(), new GetEleInstruction(LLvmIRNameGenerator.getLocalVarName(), value, instr));
            }
        }
    }


}
