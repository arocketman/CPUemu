package main.compiler;

import main.core.Sim;
import main.core.Utils;

/**
 * Created by Andreuccio on 08/07/2016.
 */
public class Compiler {
    Sim system;

    public Compiler(Sim system) {
        this.system = system;
    }

    public void compileInstruction(String instruction, String sourceOP, String destOP){
        //We put the instruction in memory. The instruction is put in a way that it's always 4 bytes long. So
        //ADD -> 0ADD , MOVE -> MOVE . This little hack helps a lot during the fetch phase.
        String instruction4 = instruction.substring(0,3);

        if(Utils.isNumeric(sourceOP)){
            //Must be immediate addressing.
            instruction4 = instruction4 + "I"; //MOVE -> MOV -> MOVI
            byte [] op1 = Utils.encodeIntegerToBytes(sourceOP);

            //TODO: As of right now I'm just using one byte and 'filling up' the other one. This can definitely be improved.
            system.getMemory().putInstruction(instruction4);
            system.getMemory().putInstruction(op1[0]);
            system.getMemory().putInstruction(op1[1]);
            system.getMemory().putInstruction(destOP);
        }
        else{
            instruction4 = instruction4 + "R"; //MOVE -> MOV -> MOVR
            system.getMemory().putInstruction( instruction4 + sourceOP + destOP);
        }

    }

}
