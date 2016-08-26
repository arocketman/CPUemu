package main.core;

/**
 * This class handles the execution of a given instruction.
 * @author Andrea Capuano
 * @version 0.1
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
            case Constants.ISA_MOVEREGISTER :
                cpu.moveD(sourceReg,destReg);
                cpu.setSR(sourceReg);
                break;
            case Constants.ISA_ADDREGISTER :
                cpu.moveD(sourceReg+destReg,destReg);
                cpu.setSR(sourceReg+destReg);
                break;
            case Constants.ISA_MULTIPLYREGISTER:
                cpu.moveD(sourceReg*destReg,destReg);
                cpu.setSR(sourceReg*destReg);
                break;
            case Constants.ISA_MODREGISTER:
                cpu.moveD(sourceReg%destReg,destReg);
                cpu.setSR(sourceReg % destReg);
                break;
            case Constants.ISA_SUBTRACTREGISTER :
                cpu.moveD(sourceReg-destReg,destReg);
                cpu.setSR(sourceReg-destReg);
                break;
            case Constants.ISA_COMPAREREGISTER:
                cpu.setSR(sourceReg-destReg);
                break;
        }
    }

    public void numToReg(String OP,String source,String destination){
        Integer sourceOp = decodeOperand(source);
        Integer destReg = decodeRegister(destination);
        switch(OP){
            case Constants.ISA_MOVEIMMEDIATE :
                cpu.moveD(sourceOp,destReg);
                cpu.setSR(sourceOp);
                break;
            case Constants.ISA_ADDIMMEDIATE :
                cpu.moveD(sourceOp+destReg,destReg);
                cpu.setSR(sourceOp+destReg);
                break;
            case Constants.ISA_SUBTRACTIMMEDIATE :
                cpu.moveD(destReg-sourceOp,destReg);
                cpu.setSR(destReg-sourceOp);
            break;
            case Constants.ISA_MULTIPLYIMMEDIATE:
                cpu.moveD(sourceOp*destReg,destReg);
                cpu.setSR(sourceOp*destReg);
                break;
            case Constants.ISA_MODIMMEDIATE:
                cpu.moveD(destReg%sourceOp,destReg);
                cpu.setSR(destReg%sourceOp);
                break;
            case Constants.ISA_COMPAREIMMEDIATE:
                cpu.setSR(sourceOp-destReg);
                break;
            case Constants.ISA_JUMP:
                jmp(sourceOp);
                break;
            case Constants.ISA_BRANCHNOTEQUAL:
                if(!cpu.isFlagZeroHigh())
                    jmp(sourceOp);
                break;
            case Constants.ISA_BRANCHEQUAL:
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
            case Constants.REGISTER_A0: return cpu.getA(0);
            case Constants.REGISTER_A1: return cpu.getA(1);
            case Constants.REGISTER_A2: return cpu.getA(2);
            case Constants.REGISTER_A3: return cpu.getA(3);
            case Constants.REGISTER_A4: return cpu.getA(4);
            case Constants.REGISTER_A5: return cpu.getA(5);
            case Constants.REGISTER_A6: return cpu.getA(6);
            case Constants.REGISTER_A7: return cpu.getA(7);
            case Constants.REGISTER_D0: return cpu.getD(0);
            case Constants.REGISTER_D1: return cpu.getD(1);
            case Constants.REGISTER_D2: return cpu.getD(2);
            case Constants.REGISTER_D3: return cpu.getD(3);
            case Constants.REGISTER_D4: return cpu.getD(4);
            case Constants.REGISTER_D5: return cpu.getD(5);
            case Constants.REGISTER_D6: return cpu.getD(6);
            case Constants.REGISTER_D7: return cpu.getD(7);
            default:
                return null;
        }

    }

}
