package frontend.SyntaxAnalysis.Units;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits.ConstExp;
import io.Conponent.ErrorToken;
import io.Settings;

public class Unit {

    private AstNode root;

    public Unit(AstNode root) {
        this.root = root;
    }

    public void analysis() {

    }

    public AstNode getRoot() {
        return root;
    }

    public void throwParserError(String UnitType) {
        int lineNum = RecursionDown.getPreToken().getLineNumber();
        Settings.getInstance().addErrorInfo("Line: " + lineNum + ". Type: " + UnitType);
    }

    public void generateSubUnit(AstNode declNode, AstNode defNode) {
        declNode.addChild(defNode);
        if (Checker.isIdent()) {
            defNode.addChild(new AstNode("IDENFR"));
            RecursionDown.nextToken();
        } else {
            this.throwParserError("IDENFR");
        }
        while (RecursionDown.getPreToken().getReserveWord().equals("LBRACK")) {
            defNode.addChild(new AstNode("LBRACK"));
            RecursionDown.nextToken();
            if (Checker.isConstExp()) {
                new ConstExp(defNode);
            }
            this.syntaxCheck('k', "RBRACK", defNode);
        }
    }

    public void syntaxCheck(char type, String target, AstNode who) {
        if (target.equals(RecursionDown.getPreToken().getReserveWord())) {
            who.addChild(new AstNode(target));
            RecursionDown.nextToken();
        } else {
            Settings.getInstance().addErrorToken(new ErrorToken(type, RecursionDown.getPreAstNode().getEnd()));
        }
    }

    public void addError(char type) {
        Settings.getInstance().addErrorToken(new ErrorToken(type, RecursionDown.getPreAstNode().getEnd()));
    }
}
