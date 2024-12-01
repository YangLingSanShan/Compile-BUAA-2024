package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.RType;
import backend.AssembleCodes.Register;

public class Jr extends RType {
    public Jr(Register rs) {
        super(rs, Register.ZERO, Register.ZERO);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "jr " + rs;
    }
}
