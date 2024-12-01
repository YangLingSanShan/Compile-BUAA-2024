package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.IType;
import backend.AssembleCodes.Register;

public class Sb extends IType {
    public Sb(Register base, Register rt, Integer offset) {
        //sw $rt imm($rs) rt = MEM[base + offset]
        super(base, rt, offset);   //base = rs ; immediate = offset
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "sb , " + rt + " , " + immediate + "(" + rs + ")";
    }
}