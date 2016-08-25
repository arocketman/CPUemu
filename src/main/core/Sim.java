package main.core;


import javafx.util.Pair;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This class encapsulates the emulated system. It is responsible of looping through the Von Neumann's cycle.
 *
 * @author Andrea Capuano
 * @version 0.1
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
        if(needsAnotherFetchInstruction(decodedInstruction)){
                fetch();
                return decode(decodedInstruction);
        }
        LOGGER.info(Utils.getPCstr(cpu.getPC()) + "Decoded the following instruction : " + decodedInstruction);
        return decodedInstruction;
    }

    private boolean needsAnotherFetchInstruction(String decodedInstruction) {
        return  decodedInstruction.equals("MOVR") ||
                decodedInstruction.equals("ADDR") ||
                decodedInstruction.equals("SUBR") ||
                decodedInstruction.equals("ADDI") ||
                decodedInstruction.equals("MODR") ||
                decodedInstruction.equals("MODI") ||
                decodedInstruction.equals("MOVI") ||
                decodedInstruction.equals("SUBI") ||
                decodedInstruction.equals("JMPI") ||
                decodedInstruction.equals("BEQI") ||
                decodedInstruction.equals("BNEI") ||
                decodedInstruction.equals("MULR") ||
                decodedInstruction.equals("MULI") ||
                decodedInstruction.equals("CMPI");
    }

    /**
     * Executes through the Cpu the given instruction.
     * @param instruction
     */
    private void execute(String instruction){
        if(Utils.isRegisterOperation(instruction.substring(0,4)))
            cpu.getInstructionSet().regToReg(instruction.substring(0,4),instruction.substring(4,6),instruction.substring(6,8));
        else if(Utils.isImmediateOperation(instruction.substring(0,4)))
            cpu.getInstructionSet().numToReg(instruction.substring(0,4),instruction.substring(4,6),instruction.substring(6,8));
        else {
            LOGGER.info("The fetched instruction from the IR has not been recognized.");
            //throw InstructionNotRecognizedException;
        }
    }

    /**
     * Edits a CPU or special register given the name of the register to edit and the value.
     * @param registerValuePair contains the name of the register to edit and the value to edit it to.
     */
    public void editRegister(Pair<String, String> registerValuePair) {
        switch(registerValuePair.getKey()){
            case "PC":
                cpu.setPC(Integer.parseInt(registerValuePair.getValue()));
            break;
            case "Memory Location":
                memory.setCurrentInstructionAddress(Integer.parseInt(registerValuePair.getValue()));
            break;
            case "D0":
                cpu.setD(0, Integer.valueOf(registerValuePair.getValue()));
            break;
            case "D1":
                cpu.setD(1, Integer.valueOf(registerValuePair.getValue()));
            break;
            case "D2":
                cpu.setD(2, Integer.valueOf(registerValuePair.getValue()));
            break;
            case "D3":
                cpu.setD(3, Integer.valueOf(registerValuePair.getValue()));
            break;
            case "D4":
                cpu.setD(4, Integer.valueOf(registerValuePair.getValue()));
            break;
            case "D5":
                cpu.setD(5, Integer.valueOf(registerValuePair.getValue()));
            break;
            case "D6":
                cpu.setD(6, Integer.valueOf(registerValuePair.getValue()));
            break;
            case "D7":
                cpu.setD(7, Integer.valueOf(registerValuePair.getValue()));
            break;

        }
    }
}
