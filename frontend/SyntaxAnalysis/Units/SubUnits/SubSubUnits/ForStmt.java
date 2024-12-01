package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class ForStmt extends Unit {
    public ForStmt(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode forStmtValNode = new AstNode("<ForStmt>");
        getRoot().addChild(forStmtValNode);

        // 检查是否是 LVal
        if (!Checker.isLVal()) {
            this.throwParserError("LVal");
        }

        new LVal(forStmtValNode);  // 添加 LVal 节点

        // 检查是否存在 ASSIGN 赋值符号
        if (!RecursionDown.getPreToken().getReserveWord().equals("ASSIGN")) {
            this.throwParserError("ASSIGN");
        }

        forStmtValNode.addChild(new AstNode("ASSIGN"));  // 添加 ASSIGN 节点
        RecursionDown.nextToken();

        // 检查是否为表达式 Exp
        if (!Checker.isExp()) {
            this.throwParserError("Exp");
        }

        new Exp(forStmtValNode);  // 添加 Exp 节点
    }

}
