package sample.core;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Andreuccio on 06/07/2016.
 */
public class CPU {

    public static void main(String[] args) {
        CPU cpu = new CPU();
        cpu.D.set(0,0x2);
        cpu.instructionSet.moveRegToReg("D0","D1");
        System.out.println("ciao");
    }


    private InstructionSet instructionSet;
    private ArrayList<Integer> D; //data registers - 32 bits
    private ArrayList<Integer> A; //address registers - 32bits
    private short SR; //status register - 16bits
    private int PC;

    public CPU(){
        D = new ArrayList<>();
        A = new ArrayList<>();

        for(int i = 0; i <= 7; i++){
            D.add(new Integer(i));
            A.add(new Integer(i));
        }
        instructionSet = new InstructionSet(this);
    }

    public InstructionSet getInstructionSet() {
        return instructionSet;
    }

    /* Registers getters and setters */

    public Integer getD(int index){
        return D.get(index);
    }

    public void setD(Integer oldVal, Integer newVal){
        for(int i = 0; i <= 7; i++){
            //We want to actually compare references here.
            if(newVal == getD(i)){
                D.set(i,new Integer(oldVal));
                return;
            }
        }
    }

    public Integer getA(int index){
        return A.get(index);
    }

    public void setA(int index, int value){
        A.set(index,value);
    }

    public short getSR() {
        return SR;
    }

    public void setSR(short SR) {
        this.SR = SR;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }
}
