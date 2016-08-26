package main.core;

/**
 * The emulated Memory. Its size is specified by the MEM_SIZE variable.
 * @author Andrea Capuano
 * @version 1.0
 */
public class Memory {
    private byte[] ram;

    public static final int MEM_SIZE = 8000;
    public static final int INSTRUCTION_AREA = 4000;
    private int currentInstructionAddress;

    public Memory(){
        ram = new byte[MEM_SIZE];
        reset();
    }

    /**
     * Brings the Memory back to its initial state.
     */
    public void reset() {
        for(int i = 0; i < MEM_SIZE; i++){
            ram[i] = 00;
        }
        currentInstructionAddress = INSTRUCTION_AREA;
    }

    public int getCurrentInstructionAddress() {
        return currentInstructionAddress;
    }

    public void setCurrentInstructionAddress(int currentInstructionAddress) {
        this.currentInstructionAddress = currentInstructionAddress;
    }

    public byte[] getRam() {
        return ram;
    }

    public byte getValue1(int address){
        return ram[address];
    }

    public byte[] getValue4(int address){
        byte[] bytes = new byte[4];
        for(int i = 0; i < 4; i++){
            bytes[i] = ram[address+i];
        }
        return bytes;
    }

    public void setValue(int address , byte value){
        ram[address] = value;
    }

    public void putInstruction(String instruction){
        for(byte b : instruction.getBytes())
            ram[currentInstructionAddress++] = b;
    }

    public void putInstruction(byte b){
        ram[currentInstructionAddress++] = b;
    }

}
