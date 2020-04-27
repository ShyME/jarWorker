package instruction;

import file.FileUtil;
import instruction.util.FieldNameParser;
import instruction.util.Instruction;
import instruction.util.MyMethodInfo;
import jar.JarSaver;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

import java.io.IOException;

public class SetInstruction implements Instruction, FieldNameParser {
    private String whereTo;
    private String toBeSet;
    private String methodSrcFile;
    private String instructionType;

    private SetInstruction() {}
    public SetInstruction(String whereTo, String toBeSet, String methodSrcFile, String instructionType) {
        this.whereTo = whereTo.replaceAll("/",".");
        this.toBeSet = toBeSet;
        this.methodSrcFile = methodSrcFile;
        this.instructionType = instructionType;
    }

    @Override
    public void execute() {
        String content = FileUtil.getFileAsString(methodSrcFile);
        MyMethodInfo myMethodInfo = getMethodInfo(whereTo, toBeSet);
        CtClass parentClass = myMethodInfo.getParent();
        JarSaver jarSaver = new JarSaver();

        switch(instructionType) {
            case "ctor-body":
                CtConstructor ctConstructor = findConstructor(myMethodInfo);
                try {
                    ctConstructor.setBody(content);
                    jarSaver.update(parentClass);
                } catch (CannotCompileException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case "method-body":
                CtMethod ctMethod = findMethod(myMethodInfo);
                try {
                    ctMethod.setBody(content);
                    jarSaver.update(parentClass);
                } catch (CannotCompileException | IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + instructionType);
        }
    }
}
