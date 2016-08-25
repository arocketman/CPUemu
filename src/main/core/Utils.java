package main.core;

/**
 * Utils class contains utility static methods used by other classes.
 * @author Andrea Capuano
 * @version 0.1
 */
public class Utils {

    /**
     * Returns all the data registers in array of Strings form.
     * @return
     */
    public static String[] getDataRegsStrings(){
        return new String[]{"D0","D1","D2","D3","D4","D5","D6","D7"};
    }

    /**
     * Formats the string with a specified number of leading zeroes.
     * @param numZeroes the number of leading zeroes you want.
     * @param str the string to be formatted.
     * @return a formatted string str containing numZeroes of zeroes.
     */
    public static String getLeadingZeroesVersion(int numZeroes,String str){
        return String.format("%"+numZeroes+"s", str).replace(" ","0");
    }

    /**
     * Gets the hex representation of the integer value.
     * @param value the integer value.
     * @return a string which is the hex of the input integer.
     */
    public static String getHexWithTrailingZeroes(Integer value){
        return getLeadingZeroesVersion(8,Integer.toHexString(value));
    }

    /**
     * Given an integer value it returns the stringified binary version of such value.
     * @param value the integer to get the binary form of.
     * @return a string which is the binary form of the input integer.
     */
    public static String getBinWithTrailingZeroes(Integer value){
        return getLeadingZeroesVersion(16,Integer.toBinaryString(value));
    }

    /**
     * Checks whether or not the input string is a valid integer number or not.
     * @param str the string to check against.
     * @return true if the string is an integer number, false otherwise.
     */
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

    /**
     * Returns the byte representation of the input string.
     * @param sourceOP the string to get the byte representation of.
     * @return a byte representation of the input string.
     */
    public static byte getByteOperandFromNumericString(String sourceOP) {
        if(Integer.parseInt(sourceOP) > 127){
            return 127;
        }
        return Byte.parseByte(sourceOP);
    }

    /**
     * Checks if the string "instruction" is a register operation. E.g: ADDR is a register operation.
     * @param instruction the input instruction.
     * @return true if the instruction is a register operation, false otherwise.
     */
    public static boolean isRegisterOperation(String instruction){
        return instruction.endsWith("R");
    }

    /**
     * Checks if the input instruction is an immediate operation. E.g : MOVI is an immediate operation.
     * @param instruction the input instruction.
     * @return true if the instruction is an immediate operation, false otherwise.
     */
    public static boolean isImmediateOperation(String instruction){
        return instruction.endsWith("I");
    }

    /**
     * Encodes a 4 digits integer value to a byte array where the first element represents the two most significant digits
     * and the second element the two least significant digits. e.g: (1234 => memBytes[0] = 12 , memBytes[1] = 34)
     * @param value the integer in string representation to be converted to a byte array.
     * @return a byte array representation of the input Integer value.
     */
    public static byte[] encodeIntegerToBytes(String value){
        if(value.length() > 4)
            value = "9999";
        // We need the input string to be 4 digits for this to work, so if the user inputs 34 , it will be 0034
        String fourDigitsStringValue = getLeadingZeroesVersion(4,value);
        byte [] memBytes = new byte[2];
        memBytes[0] = getByteOperandFromNumericString(fourDigitsStringValue.substring(0,2));
        memBytes[1] = getByteOperandFromNumericString(fourDigitsStringValue.substring(2,4));
        return memBytes;
    }

    /**
     * Formats the program counter in a pretty way, [PC].
     * @param PC
     * @return
     */
    public static String getPCstr(int PC){
        return "[" + PC + "]";
    }

}
