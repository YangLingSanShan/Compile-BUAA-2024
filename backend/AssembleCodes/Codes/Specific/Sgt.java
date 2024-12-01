package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.RType;
import backend.AssembleCodes.Register;

public class Sgt extends RType {
    public Sgt(Register rs, Register rt, Register rd) {
        super(rs, rt, rd);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "sgt , " + rd + " , " + rs + " , " + rt;
    }
}
