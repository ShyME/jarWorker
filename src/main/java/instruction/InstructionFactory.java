package instruction;

import instruction.util.Instruction;
import instruction.util.InstructionData;

public class InstructionFactory {
    public Instruction getInstruction(InstructionData instructionData) {
        Instruction instruction = null;
        switch(instructionData.getInstructionBase()) {
            case "i":
                instruction = new InputInstruction(instructionData.getTarget());
                break;
            case "o":
                instruction = new OutputInstruction(instructionData.getTarget());
                break;
            case "list":
                instruction = new ListInstruction(instructionData.getTarget(), instructionData.getExtension());
                break;
            case "script":
                instruction = new ScriptInstruction(instructionData.getTarget());
                break;
            case "add":
                instruction = new AddInstruction(instructionData.getTarget(), instructionData.getParam(), instructionData.getContent(), instructionData.getExtension());
                break;
            case "set":
                instruction = new SetInstruction(instructionData.getTarget(), instructionData.getParam(), instructionData.getContent(), instructionData.getExtension());
                break;
            case "remove":
                instruction = new RemoveInstruction(instructionData.getTarget(), instructionData.getParam(), instructionData.getExtension());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + instructionData.getInstructionBase());
        }
        return instruction;
    }
}
