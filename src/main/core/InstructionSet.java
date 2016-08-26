package main.core;

import java.util.HashMap;

/**
 * This class handles the execution of a given instruction.
 * @author Andrea Capuano
 * @version 0.1
 */
public class InstructionSet {

    private CPU cpu;
    HashMap<String,String> g;
    public final static String ISA_MOVEREGISTER = "MOVR";
    public final static String ISA_MOVEIMMEDIATE = "MOVI";
    public final static String ISA_ADDREGISTER = "ADDR";
    public final static String ISA_ADDIMMEDIATE = "ADDI";
    public final static String ISA_MULTIPLYREGISTER = "MULR";
    public final static String ISA_MULTIPLYIMMEDIATE = "MULI";
    public final static String ISA_MODREGISTER = "MODR";
    public final static String ISA_MODIMMEDIATE = "MODI";
    public final static String ISA_SUBTRACTREGISTER = "SUBR";
    public final static String ISA_SUBTRACTIMMEDIATE = "SUBI";
    public final static String ISA_COMPAREREGISTER = "CMPR";
    public final static String ISA_COMPAREIMMEDIATE = "CMPI";
    public final static String ISA_JUMP = "JMPI";
    public final static String ISA_BRANCHEQUAL = "BEQI";
    public final static String ISA_BRANCHNOTEQUAL = "BNEI";

    public InstructionSet(CPU cpu){
        this.cpu = cpu;
    }

    public void regToReg(String OP,String source,String destination){
        Integer sourceReg = decodeRegister(source);
        Integer destReg = decodeRegister(destination);
        switch(OP){
            case ISA_MOVEREGISTER :
                cpu.moveD(sourceReg,destReg);
                cpu.setSR(sourceReg);
                break;
            case ISA_ADDREGISTER :
                cpu.moveD(sourceReg+destReg,destReg);
                cpu.setSR(sourceReg+destReg);
                break;
            case ISA_MULTIPLYREGISTER:
                cpu.moveD(sourceReg*destReg,destReg);
                cpu.setSR(sourceReg*destReg);
                break;
            case ISA_MODREGISTER:
                cpu.moveD(sourceReg%destReg,destReg);
                cpu.setSR(sourceReg % destReg);
                break;
            case ISA_SUBTRACTREGISTER :
                cpu.moveD(sourceReg-destReg,destReg);
                cpu.setSR(sourceReg-destReg);
                break;
            case ISA_COMPAREREGISTER:
                cpu.setSR(sourceReg-destReg);
                break;
        }
    }

    public void numToReg(String OP,String source,String destination){
        Integer sourceOp = decodeOperand(source);
        Integer destReg = decodeRegister(destination);
        switch(OP){
            case ISA_MOVEIMMEDIATE :
                cpu.moveD(sourceOp,destReg);
                cpu.setSR(sourceOp);
                break;
            case ISA_ADDIMMEDIATE :
                cpu.moveD(sourceOp+destReg,destReg);
                cpu.setSR(sourceOp+destReg);
                break;
            case ISA_SUBTRACTIMMEDIATE :
                cpu.moveD(destReg-sourceOp,destReg);
                cpu.setSR(destReg-sourceOp);
            break;
            case ISA_MULTIPLYIMMEDIATE:
                cpu.moveD(sourceOp*destReg,destReg);
                cpu.setSR(sourceOp*destReg);
                break;
            case ISA_MODIMMEDIATE:
                cpu.moveD(destReg%sourceOp,destReg);
                cpu.setSR(destReg%sourceOp);
                break;
            case ISA_COMPAREIMMEDIATE:
                cpu.setSR(sourceOp-destReg);
                break;
            case ISA_JUMP:
                jmp(sourceOp);
                break;
            case ISA_BRANCHNOTEQUAL:
                if(!cpu.isFlagZeroHigh())
                    jmp(sourceOp);
                break;
            case ISA_BRANCHEQUAL:
                if(cpu.isFlagZeroHigh())
                    jmp(sourceOp);
                break;
        }
    }

    public void jmp(int addr){
        cpu.setPC(addr);
    }

    /**
     * Given a String representation in memory of an integer value we convert it to an Integer.
     * The method who was responsable of encoding the source operand is
     * Utils' method {@link main.core.Utils#encodeIntegerToBytes(String)} encodeIntegerToBytes.
     * So the number 1234 is actually saved as : memBytes[0] = 12 , memBytes[1] = 34.
     * @param operand The string representation of the operand in two bytes of memory.
     * @return Integer value of the input operand .
     */
    private Integer decodeOperand(String operand){
        return Integer.valueOf(Utils.getLeadingZeroesVersion(2,String.valueOf(operand.getBytes()[0])) + Utils.getLeadingZeroesVersion(2,String.valueOf(operand.getBytes()[1])));
    }

    private Integer decodeRegister(String regName){

        switch(regName){
            case "A0": return cpu.getA(0);
            case "A1": return cpu.getA(1);
            case "A2": return cpu.getA(2);
            case "A3": return cpu.getA(3);
            case "A4": return cpu.getA(4);
            case "A5": return cpu.getA(5);
            case "A6": return cpu.getA(6);
            case "A7": return cpu.getA(7);
            case "D0": return cpu.getD(0);
            case "D1": return cpu.getD(1);
            case "D2": return cpu.getD(2);
            case "D3": return cpu.getD(3);
            case "D4": return cpu.getD(4);
            case "D5": return cpu.getD(5);
            case "D6": return cpu.getD(6);
            case "D7": return cpu.getD(7);
            default:
                return null;
        }

    }

}
