package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.JType;

public class Jal extends JType {

    public Jal(String label) {
        super(label);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "jal " + label;
    }
}
