package frontend.SyntaxAnalysis;

public class Checker {
    private static String getPreReserveWord() {
        return RecursionDown.getPreToken().getReserveWord();
    }

    private static String getPositionReserveWord(int position) {
        return RecursionDown.getPositionToken(position).getReserveWord();
    }

    public static boolean isDecl() {
        return isConstDecl() || isVarDecl();
    }

    public static boolean isFunc() {
        return isFuncType() && getPositionReserveWord(1).equals("IDENFR") && getPositionReserveWord(2).equals("LPARENT");
    }

    public static boolean isFuncType() {
        return getPreReserveWord().equals("INTTK") || getPreReserveWord().equals("CHARTK") || getPreReserveWord().equals("VOIDTK");
        //FuncType → 'void' | 'int' | 'char'
    }

    public static boolean isConstDecl() {
        return getPreReserveWord().equals("CONSTTK");
    }

    public static boolean isVarDecl() {
        return (getPreReserveWord().equals("INTTK") || getPreReserveWord().equals("CHARTK"))
                && getPositionReserveWord(1).equals("IDENFR")       //有名字
                && !getPositionReserveWord(2).equals("LPARENT");    //不是函数
    }

    public static boolean isBType() {
        return isINTTK() || isCHARTK();
        // BType → 'int' | 'char'
    }

    public static boolean isINTTK() {
        return getPreReserveWord().equals("INTTK");
    }

    public static boolean isCHARTK() {
        return getPreReserveWord().equals("CHARTK");
    }

    public static boolean isConstDef() {
        return getPreReserveWord().equals("IDENFR");
    }


    public static boolean isConstInitVal() {
        return isConstExp() || getPreReserveWord().equals("LBRACE") || isStringConst();
    }

    public static boolean isConstExp() {
        return isAddExp();
    }

    public static boolean isAddExp() {
        return isMulExp();
    }

    public static boolean isMulExp() {
        return isUnaryExp();
    }

    public static boolean isUnaryExp() {
        return isPrimaryExp() || isIdent() || isUnaryOp();
    }

    public static boolean isPrimaryExp() {
        return getPreReserveWord().equals("LPARENT") || isIdent() || isNumber() || isCharacter();
    }

    public static boolean isIdent() {
        return getPreReserveWord().equals("IDENFR");
    }

    public static boolean isUnaryOp() {
        return getPreReserveWord().equals("PLUS") || getPreReserveWord().equals("MINU")
                || getPreReserveWord().equals("NOT");
    }

    public static boolean isNumber() {
        return getPreReserveWord().equals("INTCON");
    }

    public static boolean isCharacter() {
        return getPreReserveWord().equals("CHRCON");
    }

    public static boolean isStringConst() {
        return getPreReserveWord().equals("STRCON");
    }

    public static boolean isFuncRParams() {
        return isExp();
    }

    public static boolean isExp() {
        return isAddExp();
    }

    public static boolean isVarDef() {
        return isIdent();
    }

    public static boolean isInitVal() {
        return isExp() || getPreReserveWord().equals("LBRACE") || isStringConst();
    }

    public static boolean isBlock() {
        return getPreReserveWord().equals("LBRACE");
    }

    public static boolean isBlockItem() {
        return isDecl() || isStmt();
        // Decl | Stmt
    }

    public static boolean isStmt() {
        return isLVal() || isExp() || isBlock() || isReservedWordsInStmt();
    }

    public static boolean isLVal() {
        return isIdent();
    }

    public static boolean isReservedWordsInStmt() {
        return getPreReserveWord().equals("SEMICN") || getPreReserveWord().equals("IFTK") ||
                getPreReserveWord().equals("FORTK") || getPreReserveWord().equals("BREAKTK") ||
                getPreReserveWord().equals("CONTINUETK") || getPreReserveWord().equals("RETURNTK") ||
                getPreReserveWord().equals("PRINTFTK");
    }

    public static boolean isCond() {
        return isLOrExp();
    }

    public static boolean isLOrExp() {
        return isLAndExp();
    }

    public static boolean isLAndExp() {
        return isEqExp();
    }

    public static boolean isEqExp() {
        return isRelExp();
    }

    public static boolean isRelExp() {
        return isAddExp();
    }

    public static boolean isForStmtVal() {
        return isLVal() || isExp();
    }

    public static boolean isFuncFParams() {
        return isFuncFParam();
    }

    public static boolean isFuncFParam() {
        return isINTTK() || isCHARTK();
    }
}
