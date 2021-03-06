package main.core;

import java.util.ArrayList;

/**
 * The emulated CPU class. Contains registers and methods to manipulate them.
 * @author Andrea Capuano
 * @version 0.1
 */
public class CPU {

    private InstructionSet instructionSet;
    private ArrayList<Integer> D;               //data registers - 32 bits
    private ArrayList<Integer> A;               //address registers - 32bits
    private short SR;                           //status register - 16bits
    private int PC;                             //Program Counter
    private byte[] IR;                          //Instruction register

    public CPU(){
        D = new ArrayList<>();
        A = new ArrayList<>();
        IR = new byte[4];

        for(int i = 0; i <= 7; i++){
            D.add(i);
            A.add(i);
        }
        instructionSet = new InstructionSet(this);

        //Start the program counter at the first address of the instruction area.
        PC = Constants.INSTRUCTION_AREA;
    }

    public InstructionSet getInstructionSet() {
        return instructionSet;
    }

    public Integer getD(int index){
        return D.get(index);
    }

    public void setD(int regNum, Integer value){
        D.set(regNum,value);
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
    public void moveD(Integer sourceVal, Integer destReg){
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

    //At the point of version 0.1 it's not possible to manipulate Address registers.
    public void setA(int index, int value){
        A.set(index,value);
    }

    public short getSR() {
        return SR;
    }

    public void setSR(Integer result) {
        //Bits 4 down to 0. XNZVC
        //Negative = Bit 3
        if(result < 0)
            this.SR = (short)(this.SR|0x8);
        else
            this.SR = (short)(this.SR&0xFFF7);
        //Zero = Bit 2
        if(result.equals(0))
            this.SR = (short) (this.SR|0x4);
        else
            this.SR = (short) (this.SR&0xFFFB);
    }

    public boolean isFlagZeroHigh(){
        return (this.SR & 0x4) == 0x4;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public byte[] getIR() {
        return IR;
    }

    public void setIR(byte[] IR) {
        this.IR = IR;
    }

    public void incrementPC4() {
        this.PC += 4;
    }

    /**
     * Brings the CPU back to its initial state.
     */
    public void reset(){
        for(int i = 0; i < D.size(); i++){
            D.set(i,0);
            A.set(i,0);
        }
        PC = Constants.INSTRUCTION_AREA;
        SR = 0;
        for(byte b : IR)
            b = 0;
    }
}
