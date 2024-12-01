package backend.AssembleCodes.Units;

import backend.AssembleCodes.AssembleData;

public class AsciizAssemble extends AssembleData {
    public AsciizAssemble(String label, String value) {
        super(label, value);
    }

    @Override
    public String toString() {
        return label + ": .asciiz \"" + value.replace("\n", "\\n") + "\"";
    }
}
