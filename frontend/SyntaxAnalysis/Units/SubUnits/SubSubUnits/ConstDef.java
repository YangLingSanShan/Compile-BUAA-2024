package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class ConstDef extends Unit {
    public ConstDef(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode constDefNode = new AstNode("<ConstDef>");
        this.generateSubUnit(this.getRoot(), constDefNode);
        if (RecursionDown.getPreToken().getReserveWord().equals("ASSIGN")) {
            constDefNode.addChild(new AstNode("ASSIGN"));
            RecursionDown.nextToken();
            if (Checker.isConstInitVal()) {
                new ConstInitVal(constDefNode);
            } else {
                this.throwParserError("ConstInitVal");
            }
        } else {
            this.throwParserError("ConstDef");
        }
    }
}
