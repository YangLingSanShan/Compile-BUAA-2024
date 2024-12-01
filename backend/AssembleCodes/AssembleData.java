package backend.AssembleCodes;

import backend.AssembleCodes.Units.Assemble;

public class AssembleData extends Assemble {
    protected String label;
    protected String value;

    public AssembleData(String label, String value) {
        this.label = label;
        this.value = value;
        AssembleCodes.addAssembleData(this);
    }
}
