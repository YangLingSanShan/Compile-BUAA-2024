package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.IType;
import backend.AssembleCodes.Register;

public class Bne extends IType {
    public Bne(Register rs, Register rt, String label) {
        super(rs, rt, label);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "bne , " + rs + " , " + rt + " , " + label;
    }
}
