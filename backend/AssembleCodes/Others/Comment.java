package backend.AssembleCodes.Others;

import backend.AssembleCodes.AssembleCodes;
import backend.AssembleCodes.Units.Assemble;

public class Comment extends Assemble {

    private String content;

    public Comment(String content) {
        this.content = content;
        AssembleCodes.addAssembleText(this);
    }

    public String toString() {
        return "\n# " + content;
    }
}
