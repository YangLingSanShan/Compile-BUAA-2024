package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class CharacterUnit extends Unit {
    public CharacterUnit(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode characterNode = new AstNode("<Character>");
        this.getRoot().addChild(characterNode);
        characterNode.addChild(new AstNode("CHRCON"));
        RecursionDown.nextToken();
    }
}
