package backend.AssembleCodes.Codes;

import backend.AssembleCodes.Units.Assemble;
import backend.AssembleCodes.Register;

public class RType extends Assemble {
    protected Register rs;

    protected Register rt;

    protected Register rd;

    public RType(Register rs, Register rt, Register rd) {
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
    }
}
