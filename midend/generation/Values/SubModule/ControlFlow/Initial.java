package midend.generation.Values.SubModule.ControlFlow;

import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRType;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Initial {

    private final LLvmIRType type;
    private final ArrayList<Integer> initValue;
    private final ArrayList<Integer> space;
    private final Integer offset;

    public Initial(LLvmIRType type, ArrayList<?> initValue, ArrayList<Integer> space, Integer offset) {
        if (initValue.get(0) instanceof Character) {
            ArrayList<Integer> init = new ArrayList<>();
            for (Object object : initValue) {
                Character character = (Character) object;
                init.add((int) character); // 将字符转换为对应的 ASCII 整数值
            }
            initValue = init;
        }

        this.type = type;
        this.initValue = (ArrayList<Integer>) initValue;
        this.space = space;
        this.offset = offset;
    }

    public LLvmIRType getType() {
        return type;
    }

    @Override
    public String toString() {
        if (initValue == null || initValue.isEmpty()) {
            return (type.isInt32()) ? type + " 0" : type + " zeroinitializer";
        } else {
            if (type.isInt32()) {
                return type + " " + initValue.get(0);
            } else if (type.isInt8()) {
                return type + " " + initValue.get(0);
            } else {
                if (space.size() == 1 && (type instanceof ArrayType arrayType) && arrayType.getEleType().isInt32()) {
                    return type + " [" + initValue.stream().map(number -> "i32 " + number)
                            .collect(Collectors.joining(", ")) + "]";
                } else if (space.size() == 1 && (type instanceof ArrayType arrayType) && arrayType.getEleType().isInt8()) {
                    return type + " [" + initValue.stream().map(number -> "i8 " + number)
                            .collect(Collectors.joining(", ")) + "]";
                } else {
                    return null;
                }
            }
        }
    }

    public ArrayList<Integer> getInitValue() {
        return initValue;
    }
}
