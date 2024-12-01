package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.RType;
import backend.AssembleCodes.Register;

public class Div extends RType {
    public Div(Register rs, Register rt) {
        super(rs, rt, Register.ZERO);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "div , " + rs + " , " + rt;
    }
}