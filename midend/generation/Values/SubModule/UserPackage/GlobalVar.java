package midend.generation.Values.SubModule.UserPackage;

import backend.AssembleCodes.Units.WordAssemble;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.BasicInstruction.AllocaInstruction;
import midend.generation.Values.SubModule.ControlFlow.Initial;
import midend.generation.Values.SubModule.User;
import midend.generation.Values.Value;
import midend.optimizer.Optimizer;
import midend.optimizer.Use;

import java.util.ArrayList;

public class GlobalVar extends Value {

    private Initial initial;

    public GlobalVar(LLvmIRType type, String name, Initial initial) {
        super(type, name);
        this.initial = initial;

        if (!Optimizer.isOptimizer()) {
            LLvmIRNameGenerator.addGlobalVar(this);
        }
    }

    @Override
    public String toString() {
        return name + " = dso_local global " + initial.toString();
    }

    @Override
    public void generateAssemble() {
        LLvmIRType target = ((PointerType) type).getPointTo();
        if (target.isInt32() || target.isInt8()) {
            new WordAssemble(name.substring(1), String.valueOf(initial.getInitValue().isEmpty() ? 0 : initial.getInitValue().get(0)), null);
        } else {
            new WordAssemble(name.substring(1), String.valueOf(((ArrayType) target).calSpaceTot()), initial.getInitValue());
//            if (BackEndOptimizerUnit.isSpaceOptimizer()) {
//                new SpaceAsm(name.substring(1), String.valueOf(((ArrayType) target).calSpaceTot() * 4));
//                if (initial.getOffset() != 0) {
//                    for (int offset = 0; offset < initial.getOffset(); offset++) {
//                        new LiAsm(Register.T0, initial.getInitValue().get(offset));
//                        new MemTypeAsm("sw", name.substring(1), Register.T0, null, offset * 4);
//                    }
//                }
//            } else {
//                new WordAsm(name.substring(1), String.valueOf(((ArrayType) target).calSpaceTot()), initial.getInitValue());
//            }
        }
    }

    public Initial getInitial() {
        return initial;
    }

}
