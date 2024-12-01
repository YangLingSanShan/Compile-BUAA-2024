package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.IType;
import backend.AssembleCodes.Register;

public class Sll extends IType {

    public Sll(Register rs, Register rt, Integer immediate) {
        super(rs, rt, immediate);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "sll , " + rs + " , " + rt + " , " + immediate;
    }
}
