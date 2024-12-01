package midend.generation.Values.SubModule.ControlFlow;

import frontend.SyntaxAnalysis.AstNode;
import midend.generation.Values.SubModule.BasicBlock;

public class Loop {

    private final BasicBlock condBlock;
    private final AstNode forStmtVal2;
    private final BasicBlock currentLoopBlock;
    private final BasicBlock followBlock;

    public Loop(BasicBlock condBlock, AstNode forStmtVal2,
                BasicBlock loopBodyBlock, BasicBlock followBlock) {

        this.condBlock = condBlock;
        this.forStmtVal2 = forStmtVal2;
        this.currentLoopBlock = loopBodyBlock;
        this.followBlock = followBlock;
    }

    public BasicBlock getCondBlock() {
        return condBlock;
    }

    public AstNode getForStmtVal2() {
        return forStmtVal2;
    }

    public BasicBlock getCurrentLoopBlock() {
        return currentLoopBlock;
    }

    public BasicBlock getFollowBlock() {
        return followBlock;
    }

}
