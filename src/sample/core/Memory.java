package sample.core;

/**
 * Created by Andreuccio on 06/07/2016.
 */
public class Memory {
    private byte[] ram;

    public Memory(){
        ram = new byte[8000];
    }

    public byte getValue(int address){
        return ram[address];
    }

    public void setValue(int address , byte value){
        ram[address] = value;
    }
}
