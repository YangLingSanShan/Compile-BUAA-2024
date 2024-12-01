package midend.generation.Values.Instruction.IOInstruction;

import backend.AssembleCodes.Codes.Specific.La;
import backend.AssembleCodes.Codes.Specific.Li;
import backend.AssembleCodes.Codes.Specific.Syscall;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.SubModule.FormatString;

public class PutStrInstruction extends IoStreamInstruction {
    private final FormatString str;

    public PutStrInstruction(FormatString str) {
        super(new VarType(0), "PutStrDeclare", "io");
        this.str = str;
    }

    public static String getDeclare() {
        return "declare void @putch(i32)\ndeclare void @putstr(i8*)";
    }

    @Override
    public String toString() {
        return "call void @putstr(i8* getelementptr inbounds (" +
                str.getPointer().getPointTo() + ", " +
                str.getPointer() + " " +
                str.getName() + ", i64 0, i64 0))";
    }

    @Override
    public void generateAssemble() {
        super.generateAssemble();
        new La(Register.A0, str.getName().substring(1));
        new Li(Register.V0, 4);
        new Syscall();
    }
}
