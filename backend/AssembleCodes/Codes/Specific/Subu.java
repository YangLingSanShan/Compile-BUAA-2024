package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.RType;
import backend.AssembleCodes.Register;

public class Subu extends RType {
    public Subu(Register rs, Register rt, Register rd) {
        super(rs, rt, rd);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "subu , " + rd + " , " + rs + " , " + rt;
    }
}
