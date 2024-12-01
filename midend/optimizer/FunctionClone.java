package midend.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import midend.generation.Values.Instruction.BasicInstruction.PhiInstruction;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;
import midend.generation.Values.SubModule.BasicBlock;

public class FunctionClone {
    private final HashMap<Value, Value> copyMap;
    private final HashSet<BasicBlock> visitedMap;
    private final ArrayList<PhiInstruction> phiInstructions;
    private final Function caller;

    public FunctionClone(Function caller) {
        this.copyMap = new HashMap<>();
        this.visitedMap = new HashSet<>();
        this.phiInstructions = new ArrayList<>();
        this.caller = caller;
    }
}
