package backend.AssembleCodes;

import backend.AssembleCodes.Codes.RType;

public class Sge extends RType {
    public Sge(Register rs, Register rt, Register rd) {
        super(rs, rt, rd);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "sge , " + rd + " , " + rs + " , " + rt;
    }
}

