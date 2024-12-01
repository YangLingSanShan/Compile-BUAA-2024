package backend.AssembleCodes.Others;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Units.Assemble;

public class Label extends Assemble {
    private final String label;

    public Label(String label) {
        this.label = label;
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return label + ":";
    }
}
