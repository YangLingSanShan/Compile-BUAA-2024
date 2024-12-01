package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.Fake;
import backend.AssembleCodes.Register;

public class Li extends Fake {

    private final Register rd;
    private final Integer number;

    public Li(Register register, Integer value) {
        rd = register;
        number = value;
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "li , " + rd + " , " + number;
    }
}
