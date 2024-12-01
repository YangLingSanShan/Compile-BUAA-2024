package backend.AssembleCodes.Codes;

import backend.AssembleCodes.Units.Assemble;

public class JType extends Assemble {

    protected String label;

    public JType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
