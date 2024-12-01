package midend.generation.Items.LLvmIRSpecificType;

import midend.generation.Items.LLvmIRType;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayType extends LLvmIRType {

    private final ArrayList<Integer> space;
    private final LLvmIRType eleType;

    public ArrayType(ArrayList<Integer> space, VarType varType) {
        this.space = space;
        this.eleType = varType;
    }

    @Override
    public String toString() {
        return (space.size() == 1) ? "[" + space.get(0) + " x " + eleType + "]"
                : "[" + space.get(0) + " x " + "[" + space.get(1) + " x " + eleType + "]]";
    }

    public Integer calSpaceTot() {
        Integer tot = 1;
        for (Integer i : space) {
            tot *= i;
        }
        return tot;
    }

    public ArrayList<Integer> getSpace() {
        return space;
    }

    public LLvmIRType getEleType() {
        return eleType;
    }
}
