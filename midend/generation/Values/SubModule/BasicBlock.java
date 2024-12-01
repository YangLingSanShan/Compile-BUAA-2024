package midend.generation.Values.SubModule;

import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.StructType;
import midend.generation.Values.Instruction.BasicInstruction.AllocaInstruction;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;
import midend.optimizer.LoopVal;
import midend.optimizer.Optimizer;
import backend.AssembleCodes.Others.Label;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class BasicBlock extends Value {
    private ArrayList<Instruction> instructionArrayList;
    private Function belongFunc;
    private boolean exist;
    private LoopVal loopVal;

    public BasicBlock(String name) {
        super(new StructType("basicBlock"), name);
        this.instructionArrayList = new ArrayList<>();
        //TODO: 优化
        if (!Optimizer.isOptimizer()) {
            LLvmIRNameGenerator.addBasicBlock(this);
        }
        this.exist = true;
    }

    public boolean isEmpty() {
        return this.instructionArrayList.isEmpty();
    }

    public Instruction getPreInstruction() {
        return instructionArrayList.get(instructionArrayList.size() - 1);
    }

    public void addInstruction(Instruction instr, Integer... idx) {
        if (idx.length == 1) {
            instructionArrayList.add(idx[0], instr);
        } else {
            instructionArrayList.add(instr);
        }
        instr.setBelongBasicBlock(this);
    }

    public void setBelongFunc(Function belongFunc) {
        this.belongFunc = belongFunc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n\t");
        for (int i = 0; i < instructionArrayList.size(); i++) {
            sb.append(instructionArrayList.get(i).toString());
            if (i != instructionArrayList.size() - 1) {
                sb.append("\n\t");
            }
        }
        return sb.toString();
    }

    @Override
    public void generateAssemble() {
        new Label(name);
        // instructionArrayList.forEach(Instruction::generateAssemble);
        for (Instruction instruction : instructionArrayList) {
            instruction.generateAssemble();
        }
    }


    public Function getBelongFunction() {
        return belongFunc;
    }

    public void buildFuncCallGraph() {
        instructionArrayList.forEach(Instruction::buildFuncCallGraph);
    }

    public void insertInstruction(Integer index, Instruction phiInstr) {
        instructionArrayList.add(index, phiInstr);
        phiInstr.setBelongBasicBlock(this);
    }

    public ArrayList<Instruction> getInstructionArrayList() {
        return instructionArrayList;
    }
}
