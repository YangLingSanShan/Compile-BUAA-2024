package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.JType;

public class J extends JType {

    public J(String label) {
        super(label);
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "j " + label;
    }
}
