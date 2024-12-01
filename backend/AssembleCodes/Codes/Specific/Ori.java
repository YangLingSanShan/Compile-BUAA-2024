package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.IType;
import backend.AssembleCodes.Register;

public class Ori extends IType {

    public Ori(Register rs, Register rt, Integer immediate) {
        super(rs, rt, immediate);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        //addi $s1 $s2 $imm -> s1 = s2 + imm
        return "ori , " + rs + " , " + rt + " , " + immediate;
    }
}
