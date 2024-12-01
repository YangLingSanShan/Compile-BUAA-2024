package backend.AssembleCodes;

import backend.AssembleCodes.Units.Assemble;
import backend.AssembleCodes.Units.Segment;

public class AssembleCodes {
    // data text

    private static Segment textSegment;
    private static Segment dataSegment;

    // 单例实例
    private static AssembleCodes instance;

    // 私有构造函数，防止直接实例化
    private AssembleCodes() {
        textSegment = new Segment();
        dataSegment = new Segment();
    }

    // 获取单例实例的静态方法
    public static AssembleCodes getInstance() {
        if (instance == null) {
            instance = new AssembleCodes();
        }
        return instance;
    }

    public static void addAssembleData(AssembleData assembleData) {
        getInstance().dataSegment.addAssemble(assembleData);
    }

    public static void addAssembleText(Assemble assemble) {
        getInstance().textSegment.addAssemble(assemble);
    }

    @Override
    public String toString() {
        return ".data\n" + dataSegment + "\n\n\n.text\n" + textSegment;
    }
}
