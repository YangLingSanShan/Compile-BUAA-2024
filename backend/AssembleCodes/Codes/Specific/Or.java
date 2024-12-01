package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.RType;
import backend.AssembleCodes.Register;

public class Or extends RType {
    public Or(Register rs, Register rt, Register rd) {
        super(rs, rt, rd);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "or , " + rd + " , " + rs + " , " + rt;
    }
}