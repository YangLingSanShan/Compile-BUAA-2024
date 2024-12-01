package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.IType;
import backend.AssembleCodes.Register;

public class Lw extends IType {
    public Lw(Register base, Register rt, Integer offset) {
        //lw $rt imm($rs) rt = MEM[base + offset]
        super(base, rt, offset);   //base = rs ; immediate = offset
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "lw , " + rt + " , " + immediate + "(" + rs + ")";
    }
}
