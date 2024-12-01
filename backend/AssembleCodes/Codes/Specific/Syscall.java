package backend.AssembleCodes.Codes.Specific;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Codes.Fake;

public class Syscall extends Fake {
    public Syscall() {
        AssembleCodes.addAssembleText(this);
    }

    @Override
    public String toString() {
        return "syscall";
    }
}
