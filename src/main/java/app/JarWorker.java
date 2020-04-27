package app;

import instruction.InstructionFactory;
import instruction.util.Instruction;
import instruction.util.InstructionData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JarWorker {
    private InstructionFactory instructionFactory = new InstructionFactory();
    public void runScript(List<String> arguments) {
        try {
            InstructionData instructionData = InstructionData.parseScript(arguments);
            Instruction instruction = instructionFactory.getInstruction(instructionData);
            instruction.execute();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void runFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> arguments = Arrays.asList(line.split(" "));
                runScript(arguments);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(String input) {
        List<String> argInstructions = Arrays.asList(input.split("--"));

        String inputInstruction = null;
        String scriptInstruction = null;
        String outputInstruction = null;
        String listInstruction = null;

        try {
            if(argInstructions.stream().anyMatch(arg -> arg.contains("i "))) {
                inputInstruction = argInstructions.stream().filter(arg -> arg.contains("i ")).findFirst().get();
                runScript(Arrays.asList(inputInstruction.split(" ")));

                if(argInstructions.stream().anyMatch(arg -> arg.contains("list-"))) {
                    listInstruction = argInstructions.stream().filter(arg -> arg.contains("list-")).findFirst().get();
                    runScript(Arrays.asList(listInstruction.split(" ")));
                }

                if(argInstructions.stream().anyMatch(arg -> arg.contains("script "))) {
                    scriptInstruction = argInstructions.stream().filter(arg -> arg.contains("script")).findFirst().get();
                    runScript(Arrays.asList(scriptInstruction.split(" ")));
                }

                if(argInstructions.stream().anyMatch(arg -> arg.contains("o "))) {
                    outputInstruction = argInstructions.stream().filter(arg -> arg.contains("o ")).findFirst().get();
                    runScript(Arrays.asList(outputInstruction.split(" ")));
                }
            }
            else
                throw new IllegalStateException("The program needs an input(--i jarPath) jar to work!");
        } catch(RuntimeException e) {
            System.out.println("Error");
        }
    }
}
