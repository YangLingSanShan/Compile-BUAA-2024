package backend.AssembleCodes.Codes;

import backend.AssembleCodes.Units.Assemble;
import backend.AssembleCodes.Register;

public class IType extends Assemble {

    protected Register rs;

    protected Register rt;

    protected Integer immediate;

    protected String label;

    public IType(Register rs, Register rt, String label) {
        this.rs = rs;
        this.rt = rt;
        this.label = label;
    }

    public IType(Register rs, Register rt, Integer immediate) {
        this.rs = rs;
        this.rt = rt;
        this.immediate = immediate;
    }
}
