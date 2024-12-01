package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.Fake;
import backend.AssembleCodes.Register;

public class Mfhi extends Fake {
    private final Register rd;

    public Mfhi(Register rd) {
        this.rd = rd;
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "mfhi , " + rd;
    }
}
