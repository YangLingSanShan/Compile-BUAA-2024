package frontend.SyntaxAnalysis.Units;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.Units.SubUnits.ConstDecl;
import frontend.SyntaxAnalysis.Units.SubUnits.VarDecl;


public class Decl extends Unit {

    public Decl(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode declNode = new AstNode("<Decl>");
        this.getRoot().addChild(declNode);
        if (Checker.isConstDecl()) {
            new ConstDecl(declNode);
        } else if (Checker.isVarDecl()) {
            new VarDecl(declNode);
        } else {
            this.throwParserError("Decl");
        }
    }
}
