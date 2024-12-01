package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.Fake;
import backend.AssembleCodes.Register;

public class La extends Fake {
    private final Register dst;

    private String label;

    public La(Register dst, String label) {
        this.dst = dst;
        this.label = label;
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "la , " + dst + " , " + label;
    }
}
