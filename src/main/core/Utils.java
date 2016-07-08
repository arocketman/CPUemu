package main.core;

/**
 * Created by Andreuccio on 07/07/2016.
 */
public class Utils {

    public static String getPCstr(int PC){
        return "[" + PC + "]";
    }

    public static String[] getRegsStrings(){
        return new String[]{"D0","D1","D2","D3","D4","D5","D6","D7"};
    }

    public static String getHexWithTrailingZeroes(Integer value){
        return getLeadingZeroesVersion(8,Integer.toHexString(value));
    }

    public static String getBinWithTrailingZeroes(Integer value){
        return getLeadingZeroesVersion(16,Integer.toBinaryString(value));
    }

    public static String getLeadingZeroesVersion(int numZeroes,String str){
        return String.format("%"+numZeroes+"s", str).replace(" ","0");
    }

    public static boolean isNumeric(String str)
    {
        try {
            Integer d = Integer.parseInt(str);
            return true;
        }
        catch(NumberFormatException nfe) {
            return false;
        }
    }

    public static byte getByteOperandFromNumericString(String sourceOP) {
        if(Integer.parseInt(sourceOP) > 127){
            return 127;
        }
        return Byte.parseByte(sourceOP);
    }

    public static boolean isRegisterOperation(String instruction){
        return instruction.endsWith("R");
    }

    public static boolean isImmediateOperation(String instruction){
        return instruction.endsWith("I");
    }
}
