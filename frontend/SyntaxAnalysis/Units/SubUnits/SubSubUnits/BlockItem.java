package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.Units.SubUnits.ConstDecl;
import frontend.SyntaxAnalysis.Units.SubUnits.VarDecl;
import frontend.SyntaxAnalysis.Units.Unit;

public class BlockItem extends Unit {
    public BlockItem(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode blockItemNode = new AstNode("<BlockItem>");
        this.getRoot().addChild(blockItemNode);
        if (Checker.isDecl()) {
            AstNode declNode = new AstNode("<Decl>");
            blockItemNode.addChild(declNode);
            if (Checker.isConstDecl()) {
                new ConstDecl(declNode);
            } else {
                new VarDecl(declNode);
            }
        } else {
            new Stmt(blockItemNode);
        }
    }
}
