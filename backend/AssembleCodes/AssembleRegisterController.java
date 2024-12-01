package backend.AssembleCodes;

import backend.AssembleCodes.Codes.Specific.*;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.SubModule.Parameter;
import midend.generation.Values.SubModule.UserPackage.GlobalVar;
import midend.generation.Values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AssembleRegisterController {

    private static AssembleRegisterController instance;
    private static HashMap<Value, Register> registerHashMap;


    public static AssembleRegisterController getInstance() {
        if (instance == null) {
            instance = new AssembleRegisterController();
            return instance;
        }
        return instance;
    }

    public void setRegisterHashMap(HashMap<Value, Register> registerHashMap) {
        this.registerHashMap = registerHashMap;
    }

    public void allocRegister(Value value, Register register) {
        if (registerHashMap == null) {
            return;
        }
        registerHashMap.put(value, register);
    }

    public static void allocReg(Value value, Register target) {
        moveValueOffset(value);
        new Sw(Register.SP, target ,AssembleController.getCurrentOffset());
    }

    public static Integer moveValueOffset(Value value) {
        Integer valueOffset;
        AssembleController.moveCurrentOffset(-4);
        valueOffset = AssembleController.getCurrentOffset();
        AssembleController.addOffset(value, valueOffset);
        return valueOffset;
    }

    public Register getRegister(Value value) {
        return (registerHashMap == null) ? null : registerHashMap.get(value);
    }

    public Register loadRegisterValue(Value value, Register defaultReg, Register reg) {
        Register register = reg;
        // 如果没有对应的寄存器，则使用默认寄存器
        if (register == null) {
            register = defaultReg;
            // 尝试在栈中查找操作数
            Integer offset = AssembleController.getOffset(value);
            // 如果没有找到
            offset = (offset == null) ? moveValueOffset(value) : offset;

            new Lw(Register.SP, register, offset);
        }
        return register;
    }

    public Register loadVariableValue(Value value, Register reg, Register defaultReg) {
        Register register = reg;
        // 如果操作数是Constant，则直接使用li指令
        if (value instanceof Constant constant) {
            new Li(defaultReg, constant.getValue());
            return defaultReg;
        }
        // 否则则加载到寄存器中
        register = loadRegisterValue(value, defaultReg, register);
        return register;
    }

    public void reAllocRegister(Value value, Register target) {
        if (getRegister(value) == null) {
            allocReg(value, target);
        }
    }

    public static HashMap<Value, Register> getRegisterHashMap() {
        return registerHashMap;
    }

    public ArrayList<Register> getAllocatedRegister() {
        return registerHashMap == null ? new ArrayList<>() : new ArrayList<>(new HashSet<>(registerHashMap.values()));
    }

    public Register allocParameterReg(Value parameter, Register register, int currentOffset, ArrayList<Register> allocatedRegs) {
        if (parameter instanceof Constant constant) {
            new Li(register, constant.getValue());
            return register;
        }
        Register source = getRegister(parameter);
        if (source != null) {
            if (parameter instanceof Parameter) {
                new Lw(Register.SP, register, currentOffset - (allocatedRegs.indexOf(source) + 1) * 4);
            } else {
                new Move(register, source);
            }
        } else {
            new Lw(Register.SP, register, AssembleController.getOffset(parameter));
        }
        return register;
    }

    public Register allocParameterMem(Value parameter, Register parameterRegister, int currentOffset, ArrayList<Register> allocatedRegs, int paraNum) {
        Register register = parameterRegister;
        if (parameter instanceof Constant constant) {
            new Li(register, constant.getValue());
            new Sw(Register.SP, register, currentOffset - (allocatedRegs.size() + 2 + paraNum) * 4);
            return register;
        }
        Register source = getRegister(parameter);
        if (source != null) {
            if (parameter instanceof Parameter) {
                new Lw(Register.SP, register, currentOffset - (allocatedRegs.indexOf(source) + 1) * 4);
            } else {
                register = source;
            }
        } else {
            new Lw(Register.SP, register, AssembleController.getOffset(parameter));
        }
        new Sw(Register.SP, register, currentOffset - ((allocatedRegs.size() + 2 + paraNum) * 4));
        return register;
    }

    public Register loadPointerValue(Value operand, Register pointerReg, Register defaultReg) {
        Register register = pointerReg;
        if (operand instanceof GlobalVar) {     // 如果操作数是全局变量，则直接使用la指令
            new La(defaultReg, operand.getName().substring(1));
            return defaultReg;
        } else {    // 否则则加载到寄存器中
            register = loadRegisterValue(operand, defaultReg, register);
            return register;
        }

    }

    public Register loadMemoryOffset(Value operand, Register instead, Register target, Register pointerReg, Register offsetReg) {
        Register register = offsetReg;
        if (operand instanceof Constant constant) {
            new Addi(target, pointerReg, constant.getValue() << 2);
        } else {
            register = loadRegisterValue(operand, instead, register);

            new Sll(instead, register, 2);
            new Addu(instead, pointerReg, target);
        }
        return register;
    }
}
