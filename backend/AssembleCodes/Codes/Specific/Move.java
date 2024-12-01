package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.Fake;
import backend.AssembleCodes.Register;

public class Move extends Fake {
    private final Register dst;
    private final Register src;


    public Move(Register dst, Register src) {
        //move dst src
        this.dst = dst;
        this.src = src;
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "move , " + dst + " , " + src;
    }
}
