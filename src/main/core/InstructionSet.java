package main.core;

/**
 * Created by Andreuccio on 06/07/2016.
 */
public class InstructionSet {

    private CPU cpu;

    public InstructionSet(CPU cpu){
        this.cpu = cpu;
    }

    public void regToReg(String OP,String source,String destination){
        Integer sourceReg = decodeRegister(source);
        Integer destReg = decodeRegister(destination);
        switch(OP){
            case "MOVR" :
                cpu.setD(sourceReg,destReg);
                cpu.setSR(sourceReg);
            break;
            case "ADDR" :
                cpu.setD(sourceReg+destReg,destReg);
                cpu.setSR(sourceReg+destReg);
            break;
            case "SUBR" :
                cpu.setD(sourceReg-destReg,destReg);
                cpu.setSR(sourceReg-destReg);
            break;
            case "CMPR":
                cpu.setSR(sourceReg-destReg);
            break;
        }
    }

    public void numToReg(String OP,String source,String destination){
        Integer sourceOp = decodeOperand(source);
        Integer destReg = decodeRegister(destination);
        switch(OP){
            case "MOVI" :
                cpu.setD(sourceOp,destReg);
                cpu.setSR(sourceOp);
                break;
            case "ADDI" :
                cpu.setD(sourceOp+destReg,destReg);
                cpu.setSR(sourceOp+destReg);
                break;
            case "SUBI" :
                cpu.setD(sourceOp-destReg,destReg);
                cpu.setSR(sourceOp-destReg);
                break;
            case "CMPI":
                cpu.setSR(sourceOp-destReg);
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
        return Integer.valueOf(String.valueOf(operand.getBytes()[0]) + String.valueOf(operand.getBytes()[1]));
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
