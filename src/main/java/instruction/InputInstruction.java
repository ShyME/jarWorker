package instruction;

import instruction.util.Instruction;
import jar.JarContext;
import jar.JarSaver;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InputInstruction implements Instruction {
    private String filename;

    @Override
    public void execute() {
        JarContext.instance.setInputJar(filename);
        JarSaver jarSaver = new JarSaver();
        jarSaver.createTempJar();
    }
}
