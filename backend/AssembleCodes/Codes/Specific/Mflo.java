package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.Fake;
import backend.AssembleCodes.Register;

public class Mflo extends Fake {
    private final Register rd;

    public Mflo(Register rd) {
        this.rd = rd;
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "mflo , " + rd;
    }
}