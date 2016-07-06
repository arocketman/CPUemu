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
    private ArrayList<Integer> D;               //data registers - 32 bits
    private ArrayList<Integer> A;               //address registers - 32bits
    private short SR;                           //status register - 16bits
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

    /**
     * Sets the value of a D register given a source value and a destination register.
     * Please note that the comparision with == and not equals is wanted.
     * We actually want to compare the object's references and not their value.
     * We want to make sure that different items inside the ArrayList aren't pointing to the same Object.
     * Each element of the ArrayList must point to a different Object, different Objects -> different references.
     * @param sourceVal
     * @param destReg
     */
    public void setD(Integer sourceVal, Integer destReg){
        for(int i = 0; i <= 7; i++){
            //We want to actually compare references here.
            if(destReg == getD(i)){
                D.set(i,new Integer(sourceVal));
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

    public void setSR(Integer SR) {
        if(SR.equals(0))
            this.SR = (short) (this.SR^0x4);
        else
            this.SR = (short) (this.SR&0xFFFB);


    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }
}
