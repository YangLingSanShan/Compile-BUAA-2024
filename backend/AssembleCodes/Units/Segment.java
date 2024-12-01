package backend.AssembleCodes.Units;

import java.util.ArrayList;
import backend.AssembleCodes.Units.Assemble;
public class Segment {
    private final ArrayList<Assemble> assemblySegments;

    public Segment() {
        this.assemblySegments = new ArrayList<>();
    }

    public void addAssemble(Assemble assemble) {
        assemblySegments.add(assemble);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Assemble assemble : assemblySegments) {
            sb.append(assemble.toString()).append("\n");
        }
        return sb.toString();
    }
}
