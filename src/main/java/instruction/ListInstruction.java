package instruction;

import instruction.util.Instruction;
import jar.JarContext;
import jar.JarUtil;
import javassist.CtClass;
import reflection.ReflectionUtil;

import java.util.Arrays;
import java.util.zip.ZipEntry;

public class ListInstruction implements Instruction {
    private JarContext jarContext = JarContext.instance;
    private String listTarget;
    private String instructionType;

    private ListInstruction() {}
    public ListInstruction(String listTarget, String instructionType) {
        if(listTarget != null)
            this.listTarget = listTarget.replaceAll("/", ".");
        this.instructionType = instructionType;
    }

    @Override
    public void execute() {
        switch(instructionType) {
            case "packages":
                System.out.println("Packages in the input jar:");
                JarUtil.getJarEntries(jarContext.getTempJar()).stream()
                        .filter(ZipEntry::isDirectory)
                        .filter(fn -> !fn.getName().equals("META-INF/"))
                        .forEach(fn -> System.out.println(fn.getName()));
                break;
            case "classes":
                System.out.println("Classes in the input jar:");
                JarUtil.getJarEntries(jarContext.getTempJar()).stream()
                        .filter(fn -> fn.getName().contains(".class"))
                        .forEach(fn -> System.out.println(fn.getName().replaceAll(".class", "")));
                break;
            case "methods":
                System.out.println("Methods in the " + listTarget + " class:");
                CtClass ctClass = ReflectionUtil.loadClass(listTarget);
                Arrays.stream(ctClass.getDeclaredMethods())
                        .forEach(System.out::println);
                break;
            case "fields":
                System.out.println("Fields in the " + listTarget + " class:");
                ctClass = ReflectionUtil.loadClass(listTarget);
                Arrays.stream(ctClass.getDeclaredFields())
                        .forEach(System.out::println);
                break;
            case "ctors":
                System.out.println("Constructors in the " + listTarget + " class:");
                ctClass = ReflectionUtil.loadClass(listTarget);
                Arrays.stream(ctClass.getDeclaredConstructors())
                        .forEach(System.out::println);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + instructionType);
        }
    }
}
