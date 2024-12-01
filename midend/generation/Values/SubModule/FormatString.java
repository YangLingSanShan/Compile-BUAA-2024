package midend.generation.Values.SubModule;

import backend.AssembleCodes.Units.AsciizAssemble;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Value;
import midend.optimizer.Optimizer;

import java.util.ArrayList;

public class FormatString extends Value {

    private String string;

    public FormatString(String name, String string, ArrayList<Integer> arrayList) {
        super(new PointerType(new ArrayType(arrayList, new VarType(8))), name);
        this.string = string;
        //TODO: 优化
        if (!Optimizer.isOptimizer()) {
            LLvmIRNameGenerator.addFormatString(this);
        }
    }

    public PointerType getPointer() {
        return (PointerType) type;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return name + " = constant " + ((PointerType) type).getPointTo() +
                " c\"" + string.replace("\n", "\\0A") + "\\00\"";
    }

    @Override
    public void generateAssemble() {
        new AsciizAssemble(name.substring(1), string);
    }
}
