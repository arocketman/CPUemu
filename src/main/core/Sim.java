package main.core;


import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by Andreuccio on 06/07/2016.
 */
public class Sim {
    private CPU cpu;
    private Memory memory;
    public static final Logger LOGGER = Logger.getLogger("SIMULATOR_LOGGER");

    public Sim(){
        cpu = new CPU();
        memory = new Memory();
    }

    public CPU getCpu() {
        return cpu;
    }

    public Memory getMemory() {
        return memory;
    }

    /**
     * The Von Neumann cycle. Fetch->Decode->Execute.
     */
    public void VonNeumann(){
        fetch();
        String decodedInstruction = decode("");
        execute(decodedInstruction);
    }

    /**
     * Grabs the instruction from the memory location pointed by the Program Counter
     * then stores it into the Instruction register. And increments the PC by 4.
     */
    public void fetch(){
        cpu.setIR(memory.getValue4(cpu.getPC()));
        cpu.incrementPC4();
        LOGGER.info(Utils.getPCstr(cpu.getPC()) + "Fetched value from memory : " + Arrays.toString(cpu.getIR()));
    }

    /**
     * Decodes the instruction located in the instruction register (IR). Note that this method calls itself (recursive).
     * The recursion is used to fetch the operands after the instruction is decoded.
     * For example: Once you decode MOVE, you know there will be two operands (source,destination).
     * The method calls itself to fetch and decode these two operands.
     * @param decodedInstruction
     * @return
     */
    private String decode(String decodedInstruction){
        decodedInstruction += new String(cpu.getIR());
        if(decodedInstruction.equals("MOVE") || decodedInstruction.equals("0ADD") || decodedInstruction.equals("0SUB")){
                //Getting the operands
                fetch();
                return decode(decodedInstruction);
        }
        LOGGER.info(Utils.getPCstr(cpu.getPC()) + "Decoded the following instruction : " + decodedInstruction);
        return decodedInstruction;
    }

    private void execute(String instruction){
        cpu.getInstructionSet().regToReg(instruction.substring(0,4),instruction.substring(4,6),instruction.substring(6,8));
    }
}
