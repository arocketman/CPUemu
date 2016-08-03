package main.compiler;

import main.core.Sim;
import main.core.Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Andreuccio on 08/07/2016.
 */
public class Compiler {
    Sim system;

    public Compiler(Sim system) {
        this.system = system;
    }

    public void compileInstruction(String instruction, String sourceOP, String destOP) {
        //We put the instruction in memory. The instruction is put in a way that it's always 4 bytes long. So
        //ADD -> 0ADD , MOVE -> MOVE . This little hack helps a lot during the fetch phase.
        String instruction4 = instruction.substring(0, 3);

        if (Utils.isNumeric(sourceOP)) {
            //Must be immediate addressing.
            instruction4 = instruction4 + "I"; //MOVE -> MOV -> MOVI
            byte[] op1 = Utils.encodeIntegerToBytes(sourceOP);

            //TODO: As of right now I'm just using one byte and 'filling up' the other one. This can definitely be improved.
            system.getMemory().putInstruction(instruction4);
            system.getMemory().putInstruction(op1[0]);
            system.getMemory().putInstruction(op1[1]);
            system.getMemory().putInstruction(destOP);
        } else {
            instruction4 = instruction4 + "R"; //MOVE -> MOV -> MOVR
            system.getMemory().putInstruction(instruction4 + sourceOP + destOP);
        }

    }

    public static void main(String[] args) throws IOException {

    }

    public ArrayList<String> loadAssembly(File file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line;
        ArrayList<String> instructions = new ArrayList<>();
        while ((line = fileReader.readLine()) != null) {
            instructions.add(line);
            // WE ARE ASSUMING THE FOLLOWING SYNTAX HERE = INSTRUCTION SOURCEOP,DESTOP or INSTRUCTION OPERAND
            parseInstruction(line);
        }
        return instructions;
    }

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
            destOP = "D0";
        }
        compileInstruction(instruction,sourceOP,destOP);
    }
}
