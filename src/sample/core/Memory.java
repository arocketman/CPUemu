package sample.core;

/**
 * Created by Andreuccio on 06/07/2016.
 */
public class Memory {
    private byte[] ram;
    public static final int INSTRUCTION_AREA = 4000;
    private int currentInstructionAddress;

    public Memory(){
        ram = new byte[8000];
        currentInstructionAddress = INSTRUCTION_AREA;
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

}
