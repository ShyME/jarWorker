package instruction;

import file.FileUtil;
import instruction.util.FieldNameParser;
import instruction.util.Instruction;
import instruction.util.MyFieldInfo;
import instruction.util.MyMethodInfo;
import jar.JarSaver;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import reflection.ReflectionUtil;

import java.io.IOException;

public class AddInstruction implements Instruction, FieldNameParser {
    private String whereTo;
    private String toBeAdded;
    private String methodSrcFile;
    private String instructionType;

    private AddInstruction() {}
    public AddInstruction(String whereTo, String toBeAdded, String methodSrcFile, String instructionType) {
        this.whereTo = whereTo.replaceAll("/", ".");
        this.toBeAdded = toBeAdded;
        this.methodSrcFile = methodSrcFile;
        this.instructionType = instructionType;
    }

    @Override
    public void execute() {
        JarSaver jarSaver = new JarSaver();
        switch(instructionType) {
            case "package":
                // --add-package pck1.pck2.packageName
                try {
                    jarSaver.add(whereTo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "class":
                CtClass ctClass = ReflectionUtil.createClass(whereTo);
                try {
                    jarSaver.add(ctClass);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "interface":
                ctClass = ReflectionUtil.createInterface(whereTo);
                try {
                    jarSaver.add(ctClass);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "before-method":
                String methodCode = FileUtil.getFileAsString(methodSrcFile);
                MyMethodInfo methodInfo = getMethodInfo(whereTo, toBeAdded);
                CtMethod ctMethod = findMethod(methodInfo);
                try {
                    ctMethod.insertBefore(methodCode);
                    CtClass parentClass = methodInfo.getParent();
                    try {
                        jarSaver.update(parentClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
                break;
            case "after-method":
                methodCode = FileUtil.getFileAsString(methodSrcFile);
                methodInfo = getMethodInfo(whereTo, toBeAdded);
                ctMethod = findMethod(methodInfo);
                try {
                    ctMethod.insertAfter(methodCode);
                    CtClass parentClass = methodInfo.getParent();
                    try {
                        jarSaver.update(parentClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
                break;
            case "method":
                methodInfo = getMethodInfo(whereTo, toBeAdded);
                ctMethod = new CtMethod(methodInfo.getReturnType(), methodInfo.getName(), methodInfo.getParameters(), methodInfo.getParent());
                try {
                    CtClass parentClass = methodInfo.getParent();
                    parentClass.addMethod(ctMethod);
                    try {
                        jarSaver.update(parentClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
                break;
            case "field":
                MyFieldInfo myFieldInfo = getFieldInfo(whereTo, toBeAdded);
                try {
                    CtField ctField = new CtField(myFieldInfo.getType(), myFieldInfo.getFieldName(), myFieldInfo.getParent());
                    CtClass parentClass = myFieldInfo.getParent();
                    parentClass.addField(ctField);
                    try {
                        jarSaver.update(parentClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + instructionType);
        }
    }
}
