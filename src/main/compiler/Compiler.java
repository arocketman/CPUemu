package main.compiler;

import main.core.Constants;
import main.core.Sim;
import main.core.Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * A simple Compiler that takes instructions as text and transforms them accordingly to the CPU syntax.
 * @author Andrea Capuano
 * @version 0.1
 */
public class Compiler {

    Sim system;

    public Compiler(Sim system) {
        this.system = system;
    }

    /**
     * Given a file containing assembly code, calls the parseInstruction for each line.
     * @param file the file containing assembly code.
     * @return an ArrayList containing the parsed instructions.
     * @throws IOException
     */
    public ArrayList<String> loadAssembly(File file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line;
        ArrayList<String> instructions = new ArrayList<>();
        while ((line = fileReader.readLine()) != null) {
            if(!isComment(line)) {
                instructions.add(line);
                parseInstruction(line);
            }
        }

        return instructions;
    }

    /**
     * Parses an instruction in String form as the instruction itself, the source operand and a destination operand.
     * @param line the full textual instruction e.g: "MOVE D0,D1"
     */
    private void parseInstruction(String line) {
        String instruction,sourceOP,destOP;
        if(line.contains(",")) { //Instructions such as MOVE D0,D1
            instruction = line.substring(0, line.indexOf(" "));
            sourceOP = line.substring(line.indexOf(" ") + 1, line.indexOf(","));
            destOP = line.substring(line.indexOf(",") + 1);
        }
        else{ //Instructions such as JMP 4000
            instruction = line.substring(0, line.indexOf(" "));
            sourceOP = line.substring(line.indexOf(" ") + 1);
            // In these kind of instructions we really don't care about the destination operand.
            destOP = Constants.REGISTER_D0;
        }
        compileInstruction(instruction,sourceOP,destOP);
    }

    /**
     * Given an instruction and the source,destination operands this puts in the system memory such data accordingly to the CPU syntax.
     * At the stage of version 0.1, the destination operand must be a data register.
     * The source operand can be both a data register or an immediate value.
     * Such method transforms the instructions as follows:
     * Register operations ADD -> ADDR
     * Immediate operations: ADD-> ADDI
     * @param instruction the instruction to be compiled.
     * @param sourceOP source operand.
     * @param destOP destination operand.
     */
    public void compileInstruction(String instruction, String sourceOP, String destOP) {
        String instruction4 = instruction.substring(0, 3);

        if (Utils.isNumeric(sourceOP)) {
            instruction4 = instruction4 + "I"; //MOVE -> MOV -> MOVI
            byte[] op1 = Utils.encodeIntegerToBytes(sourceOP);

            //TODO: As of version 0.1 I'm just using one byte and 'filling up' the other one. This can definitely be improved.
            system.getMemory().putInstruction(instruction4);
            system.getMemory().putInstruction(op1[0]);
            system.getMemory().putInstruction(op1[1]);
            system.getMemory().putInstruction(destOP);
        } else {
            instruction4 = instruction4 + "R"; //MOVE -> MOV -> MOVR
            system.getMemory().putInstruction(instruction4 + sourceOP + destOP);
        }

    }


    private boolean isComment(String line) {
        return line.startsWith(Constants.COMMENT_CHARACTER);
    }

}
