package sample.core;

/**
 * Created by Andreuccio on 06/07/2016.
 */
public class Sim {
    private CPU cpu;
    private Memory memory;

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
}
