package instruction;

import instruction.util.FieldNameParser;
import instruction.util.Instruction;
import instruction.util.MyFieldInfo;
import instruction.util.MyMethodInfo;
import jar.JarSaver;
import javassist.*;

import java.io.IOException;

public class RemoveInstruction implements Instruction, FieldNameParser {
    private String whereTo;
    private String toBeRemoved;
    private String instructionType;

    private RemoveInstruction() {}
    public RemoveInstruction(String whereTo, String toBeRemoved, String instructionType) {
        if(whereTo != null)
            this.whereTo = whereTo.replaceAll("/",".");
        this.toBeRemoved = toBeRemoved;
        this.instructionType = instructionType;
    }
    @Override
    public void execute() {
        JarSaver jarSaver = new JarSaver();
        switch(instructionType) {
            case "package":
            case "class":
            case "interface":
                jarSaver.remove(whereTo);
                break;
            case "method":
                MyMethodInfo myMethodInfo = getMethodInfo(whereTo, toBeRemoved);
                CtClass parentClass = myMethodInfo.getParent();
                CtMethod ctMethod = findMethod(myMethodInfo);
                try {
                    parentClass.removeMethod(ctMethod);
                    jarSaver.update(parentClass);
                } catch (NotFoundException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ctor":
                myMethodInfo = getMethodInfo(whereTo, toBeRemoved);
                parentClass = myMethodInfo.getParent();
                CtConstructor ctConstructor = findConstructor(myMethodInfo);
                try {
                    parentClass.removeConstructor(ctConstructor);
                    jarSaver.update(parentClass);
                } catch (NotFoundException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case "field":
                MyFieldInfo myFieldInfo = getFieldInfo(whereTo, toBeRemoved);
                parentClass = myFieldInfo.getParent();
                CtField ctField = findField(myFieldInfo);
                try {
                    parentClass.removeField(ctField);
                    jarSaver.update(parentClass);
                } catch (NotFoundException | IOException e) {
                    e.printStackTrace();
                }

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + instructionType);
        }
    }
}
