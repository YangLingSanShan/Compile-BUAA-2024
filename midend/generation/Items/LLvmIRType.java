package midend.generation.Items;

import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Items.LLvmIRSpecificType.VarType;

public class LLvmIRType {


    public boolean isArray() {
        return this instanceof ArrayType;
    }

    public boolean isVoid() {
        return this instanceof VarType && ((VarType) this).getBits() == 0;
    }

    public boolean isInt32() {
        return this instanceof VarType && ((VarType) this).getBits() == 32;
    }

    public boolean isInt8() {
        return this instanceof VarType && ((VarType) this).getBits() == 8;
    }

    public boolean isIntPtr32() {
        if (this instanceof PointerType pointerType) {
            if (pointerType.getPointTo().isInt32()) {
                return true;
            }

            if (pointerType.getPointTo() instanceof ArrayType arrayType) {
                return arrayType.getEleType().isInt32();
            }
        }
        return false;

    }

    public boolean isIntPtr8() {
        if (this instanceof PointerType pointerType) {
            if (pointerType.getPointTo().isInt8()) {
                return true;
            }

            if (pointerType.getPointTo() instanceof ArrayType arrayType) {
                return arrayType.getEleType().isInt8();
            }
        }
        return false;

    }

    public boolean isInt1() {
        return this instanceof VarType && ((VarType) this).getBits() == 1;
    }
}
