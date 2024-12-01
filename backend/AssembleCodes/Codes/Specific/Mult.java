package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.RType;
import backend.AssembleCodes.Register;

public class Mult extends RType {
    public Mult(Register rs, Register rt) {
        super(rs, rt, Register.ZERO);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "mult , " + rs + " , " + rt;
    }
}