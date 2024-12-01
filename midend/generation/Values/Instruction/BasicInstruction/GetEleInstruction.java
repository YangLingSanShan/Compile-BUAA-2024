package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.Value;

import java.util.ArrayList;

public class GetEleInstruction extends Instruction {
    public GetEleInstruction(String name, Value ptr, Value off) {
        super(new PointerType(new VarType(ptr.getType().isIntPtr32() ? 32 : 8)), name, "getEle");
        addOperand(ptr);
        addOperand(off);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        PointerType ptrType = (PointerType) (operands.get(0).getType());
        LLvmIRType type = ptrType.getPointTo();
        sb.append(name).append(" = getelementptr inbounds ")
                .append(type).append(", ")
                .append(ptrType).append(" ")
                .append(operands.get(0).getName())
                .append((type.isArray() ? ", i32 0, i32 " : ", i32 "));
        if (type.isArray() && ((ArrayType) type).getSpace().size() == 2) {
            if (operands.get(1).getName().matches("[0-9]+")) {
                ArrayList<Integer> spaces = ((ArrayType) type).getSpace();
                Integer offset = Integer.parseInt(operands.get(1).getName());
                sb.append(offset / spaces.get(1)).append(", i32 ").append(offset % spaces.get(1));
            } else {
                sb.append("0, i32 ").append(operands.get(1).getName());
            }
        } else {
            sb.append(operands.get(1).getName());
        }
        return sb.toString();
    }

    // 提取数组索引的拼接逻辑
    private String getArrayIndex(ArrayType type, String index) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> spaces = type.getSpace();

        if (spaces.size() == 2 && index.matches("[0-9]+")) {
            Integer offset = Integer.parseInt(index);
            sb.append(", i32 ").append(offset / spaces.get(1))
                    .append(", i32 ").append(offset % spaces.get(1));
        } else {
            sb.append(", i32 ").append(index);
        }

        return sb.toString();
    }

    @Override
    public void generateAssemble() {
         super.generateAssemble();
        // 首先在寄存器控制器中查找当前指令对应的寄存器，如果为null则使用K0寄存器
        Register target = AssembleRegisterController.getInstance().getRegister(this);
        target = (target == null) ? Register.K0 : target;
        // 同理，我们也需要查找当前指令的操作数对应的寄存器，如果rs为null则使用K0寄存器,
        // 如果rt为null则使用K1寄存器,当然这里由于我们的操作数是一个指针，所以我们还需要
        // 从指针中获取对应的地址
        Register pointerRegister = AssembleRegisterController.getInstance().getRegister(operands.get(0));
        pointerRegister = AssembleRegisterController.getInstance().loadPointerValue(operands.get(0), pointerRegister, Register.K0);
        Register offsetReg = AssembleRegisterController.getInstance().getRegister(operands.get(1));
        AssembleRegisterController.getInstance().loadMemoryOffset(operands.get(1), Register.K1, target, pointerRegister, offsetReg);
        // 如果之前使用了默认寄存器，则需要重新分配地址，并在寄存器控制器中更新对应的寄存器
        AssembleRegisterController.getInstance().reAllocRegister(this, target);
    }
}
