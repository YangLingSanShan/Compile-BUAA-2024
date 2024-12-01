package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

import java.io.IOException;

public class Stmt extends Unit {
    public Stmt(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode stmtNode = new AstNode("<Stmt>");
        this.getRoot().addChild(stmtNode);
        if (Checker.isExp() || RecursionDown.getPreToken().getReserveWord().equals("SEMICN")) {
            if (Checker.isExp()) {
                new Exp(stmtNode);
            }
            if (RecursionDown.getPreToken().getReserveWord().equals("SEMICN")) {
                stmtNode.addChild(new AstNode("SEMICN"));
                RecursionDown.nextToken();
            } else if (RecursionDown.getPreToken().getReserveWord().equals("ASSIGN")) {
                this.generateExtractedAssign(stmtNode);
            } else {
                this.addError('i');
            }
        } else {
            switch (RecursionDown.getPreToken().getReserveWord()) {
                case "LBRACE":
                    new Block(stmtNode);
                    break;
                case "IFTK":
                    new If(stmtNode);
                    break;
                case "BREAKTK", "CONTINUETK":
                    stmtNode.addChild(new AstNode(RecursionDown.getPreToken().getReserveWord()));
                    RecursionDown.nextToken();
                    this.syntaxCheck('i', "SEMICN", stmtNode);
                    break;
                case "RETURNTK":
                    stmtNode.addChild(new AstNode("RETURNTK"));
                    RecursionDown.nextToken();
                    if (Checker.isExp()) {
                        new Exp(stmtNode);
                    }
                    this.syntaxCheck('i', "SEMICN", stmtNode);
                    break;
                case "PRINTFTK":
                    new Printf(stmtNode);
                    break;
                case "FORTK":
                    new For(stmtNode);
                    break;
                default:
                    this.throwParserError("BlockItem");
                    break;
            }
        }

    }

    private void generateExtractedAssign(AstNode stmtNode) {
        generateExtractedExp(stmtNode);
        stmtNode.addChild(new AstNode("ASSIGN"));
        RecursionDown.nextToken();

        String preTokenWord = RecursionDown.getPreToken().getReserveWord();
        if (Checker.isExp()) {
            new Exp(stmtNode);
        } else if (preTokenWord.equals("GETINTTK") || preTokenWord.equals("GETCHARTK")) {
            stmtNode.addChild(new AstNode(preTokenWord));
            RecursionDown.nextToken();
            if (RecursionDown.getPreToken().getReserveWord().equals("LPARENT")) {
                stmtNode.addChild(new AstNode("LPARENT"));
                RecursionDown.nextToken();
                this.syntaxCheck('j', "RPARENT", stmtNode);
            }
        }
        this.syntaxCheck('i', "SEMICN", stmtNode);
    }


    private void generateExtractedExp(AstNode stmtNode) {
        // 查找并删除 <Exp> 节点
        AstNode expNode = stmtNode.getChildren().stream()
                .filter(child -> "<Exp>".equals(child.getGrammar()))
                .findFirst()
                .orElse(null);

        if (expNode != null) {
            stmtNode.deleteChild(expNode);

            // 找到 <Exp> 子节点中的 <LVal> 节点
            AstNode lvalNode = findLVal(expNode);

            // 如果找到 <LVal> 节点，则添加回 stmtNode
            if (lvalNode != null) {
                stmtNode.addChild(lvalNode);
            }
        }
    }

    private AstNode findLVal(AstNode node) {
        while (node != null && !node.getChildren().isEmpty()) {
            node = node.getChildren().get(0);
            if ("<LVal>".equals(node.getGrammar())) {
                return node;
            }
        }
        return null;
    }

}
