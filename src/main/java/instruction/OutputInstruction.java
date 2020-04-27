package instruction;

import instruction.util.Instruction;
import jar.JarSaver;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OutputInstruction implements Instruction {
    private String filename;

    @Override
    public void execute() {
        JarSaver jarSaver = new JarSaver();
        jarSaver.saveWorkAs(filename);
        jarSaver.removeTempJar();
    }
}
