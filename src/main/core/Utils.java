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
}
