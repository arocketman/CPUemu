package main.core;

/**
 * Contains constants that are used all over the project.
 * @author Andrea Capuano
 * @version 0.1
 */
public class Constants {
    /* Registers constants */

    public static final String REGISTER_D0 = "D0";
    public static final String REGISTER_D1 = "D1";
    public static final String REGISTER_D2 = "D2";
    public static final String REGISTER_D3 = "D3";
    public static final String REGISTER_D4 = "D4";
    public static final String REGISTER_D5 = "D5";
    public static final String REGISTER_D6 = "D6";
    public static final String REGISTER_D7 = "D7";
    public static final String REGISTER_A0 = "A0";
    public static final String REGISTER_A1 = "A1";
    public static final String REGISTER_A2 = "A2";
    public static final String REGISTER_A3 = "A3";
    public static final String REGISTER_A4 = "A4";
    public static final String REGISTER_A5 = "A5";
    public static final String REGISTER_A6 = "A6";
    public static final String REGISTER_A7 = "A7";
    public static final String MEMORY_LOCATION_REGISTER = "Memory Location";
    public static final String PROGRAM_COUNTER = "PC";


    /* Instruction Set Architecture constants */

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

    /* Generic instructions */

    public final static String MOVE_INSTRUCTION = "MOVE";
    public final static String JUMP_INSTRUCTION = "JMP";
    public final static String ADD_INSTRUCTION = "ADD";
    public final static String BRANCHEQUAL_INSTRUCTION = "BEQ";
    public final static String MULTIPLY_INSTRUCTION = "MUL";
    public final static String SUBTRACT_INSTRUCTION = "SUB";
    public final static String COMPARE_INSTRUCTION = "CMP";
    public final static String BRANCHNOTEQUAL_INSTRUCTION = "BNE";
    public final static String MOD_INSTRUCTION = "MOD";


    /* Other constants */
    public final static String COMMENT_CHARACTER = "#";
    public static final String LOGGER_NAME = "SIMULATOR_LOGGER";
    public static final String RESET_MEMORY_MENU = "resetMem";
    public static final String RESET_CPU_MENU = "resetCpu";
    public static final String RESETALL_MENU = "resetAll";
    public static final int MEM_SIZE = 8000;
    public static final int INSTRUCTION_AREA = 4000;
}
