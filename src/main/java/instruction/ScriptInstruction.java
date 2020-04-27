package instruction;

import instruction.util.Instruction;
import app.JarWorker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScriptInstruction implements Instruction {
    private String filename;

    @Override
    public void execute() {
        JarWorker jarWorker = new JarWorker();
        jarWorker.runFile(filename);
    }
}
