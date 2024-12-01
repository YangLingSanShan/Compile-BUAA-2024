package midend.generation.Values.SubModule;

import backend.AssembleCodes.Codes.Specific.J;
import backend.AssembleCodes.Codes.Specific.Jal;
import backend.AssembleCodes.Others.Comment;
import backend.AssembleCodes.Others.Label;
import midend.generation.Items.LLvmIRSpecificType.StructType;
import midend.generation.Values.Instruction.IOInstruction.*;
import midend.generation.Values.Value;
import midend.generation.Values.SubModule.UserPackage.GlobalVar;

import java.util.ArrayList;

import midend.generation.Values.SubModule.UserPackage.Function;

public class LLvmIRModule extends Value {

    private final ArrayList<GlobalVar> globalVars;
    private final ArrayList<Function> functions;
    private final ArrayList<FormatString> stringLiterals;

    public LLvmIRModule() {
        super(new StructType("LLvmIRModule"), "LLvmIRModule");
        this.functions = new ArrayList<>();
        this.globalVars = new ArrayList<>();
        this.stringLiterals = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GetIntInstruction.getDeclare()).append("\n");
        sb.append(GetCharInstruction.getDeclare()).append("\n");
        sb.append(PutIntInstruction.getDeclare()).append("\n");
        sb.append(PutStrInstruction.getDeclare()).append("\n\n");
        for (FormatString stringLiteral : stringLiterals) {
            sb.append(stringLiteral).append("\n");
        }
        sb.append("\n");
        for (GlobalVar globalVar : globalVars) {
            sb.append(globalVar.toString()).append("\n");
        }
        sb.append("\n");
        for (Function function : functions) {
            sb.append(function.toString()).append("\n\n");
        }
        return sb.toString();
    }

    public void addString(FormatString formatString) {
        stringLiterals.add(formatString);
    }

    public void addFunction(Function function) {
        this.functions.add(function);
    }

    public void addGlobalVar(GlobalVar globalVar) {
        this.globalVars.add(globalVar);
    }

    @Override
    public void generateAssemble() {
        globalVars.forEach(GlobalVar::generateAssemble);
        stringLiterals.forEach(FormatString::generateAssemble);
        new Comment("Jump to main Function");
        new Jal("main");
        new J("end");
        functions.forEach(Function::generateAssemble);
        new Label("end");
    }

    public ArrayList<GlobalVar> getGlobalVars() {
        return globalVars;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }
}
