package instruction.util;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class InstructionData {
    private String instruction;
    private String target;
    private String param;
    private String content;

    public String getInstructionBase() {
        return isExtended() ? instruction.substring(0, instruction.indexOf("-")) : instruction;
    }

    public String getExtension() {
        return isExtended() ? instruction.substring(instruction.indexOf("-")+1) : null;
    }

    private boolean isExtended() {
        return instruction.contains("-");
    }

    public static InstructionData parse(List<String> arguments) {
        if(arguments.size() >= 1 && arguments.size() <= 4) {
            if(!arguments.get(0).matches("--.*"))
                throw new IllegalArgumentException("No instruction type specified!");
            String instruction = arguments.get(0).substring(2);
            return InstructionData.builder()
                    .instruction(instruction)
                    .target(arguments.size() >= 2 ? arguments.get(1) : null)
                    .param(arguments.size() >= 3 ? arguments.get(2) : null)
                    .content(arguments.size() == 4 ? arguments.get(3) : null)
                    .build();
        }
        System.out.println(arguments);
        System.out.println(arguments.size());
        throw new IllegalArgumentException("Cannot construct an instruction with such given parameters!");
    }

    public static InstructionData parseScript(List<String> arguments) {
        if(arguments.size() >= 1 && arguments.size() <= 4) {
            String instruction = arguments.get(0);
            return InstructionData.builder()
                    .instruction(instruction)
                    .target(arguments.size() >= 2 ? arguments.get(1) : null)
                    .param(arguments.size() >= 3 ? arguments.get(2) : null)
                    .content(arguments.size() == 4 ? arguments.get(3) : null)
                    .build();
        }
        System.out.println(arguments);
        System.out.println(arguments.size());
        throw new IllegalArgumentException("Cannot construct an instruction with such given parameters!");
    }
}
