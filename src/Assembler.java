/**
* Static class that handles converting Assembly language to 
* @author
*  Levi Kuhaulua
*/


import java.io.File;
public class Assembler {
    // Variables to help with converting assembly language into Hexadecimal representation. 
    final static String PREFIX = "00000"; 
    final static String[] CMDS= {"halt", "load", "loadc", "store", "add", "mul", "sub", "div",
                          "and", "or", "not", "lshift", "rshift", "bwc", "bwd", "if"};
    /**
     * Checks to see if the file exists and is an Assembly language file (.asm file extension). 
     * @param fileToCheck
     *  The .asm file that is uploaded to the Console interface.  
     * @return
     *   <code>true</code> if the file exists and is a <code>.asm</code> file. Otherwise <code>false</code>. 
     */
    public static boolean checkFile(File fileToCheck) {
        return fileToCheck.exists() && fileToCheck.getName().trim().split("[.]")[1].equalsIgnoreCase("asm"); 
    }

    /**
     * Gets the hex code representation of the command. 
     * @param command
     *  The command found in the Assembly language file. 
     * @return
     *  The hex representation of the command or <code>null</code> if it is an invalid command. 
     */
    public static String parseCommand(String command) {
        for (int i = 0; i < CMDS.length; i++) {
            if (CMDS[i].equalsIgnoreCase(command)) {
                return Integer.toHexString(i); 
            }
        }
        // Return null if command was not null
        return null; 
    }

    /**
     * Will validate the commands and the arguments for the commands before translating them to their 
     * Hexadecimal representation. 
     * @param cmdHex
     *  The hex representation of the command
     * @param args
     *  Arguments associated with the command.
     * @return
     *  String value that 
     * @throws
     *  IllegalArgumentException should there be an invalid number of arguments. 
     * @throws 
     *  OutOfMemoryError should the arguments specify a register outside of the valid range: 0-7
     */
    public static String parseArguments(String cmdHex, String ...args) {
        int command = Integer.parseInt(cmdHex, 16);
        int arg1, arg2; 
        // Check if there's a second argument in loadc.  
        if (command == 2 && args.length > 1) {
            throw new IllegalArgumentException("Cannot have more than argument for loadc");  
        }


        // Check if there's more than one argument for the command. 
        if (args.length > 2) {
            throw new IllegalArgumentException("Cannot have more than two arguments for: " + CMDS[command]); 
        }

        
        if (command == 2) {
            arg2 = 0; 
            arg1 = Integer.valueOf(args[0]); 
        } else {
            arg1 = Integer.valueOf(args[0]); 
            arg2 = Integer.valueOf(args[1]); 
        }
        

        if ((arg1 > 7 || arg1 < 0) || (arg2 > 7 || arg2 < 0)) {
            throw new OutOfMemoryError("Invalid Register Value: Must be in range 0-7");
        }
        
        
        return PREFIX + cmdHex + arg1 + arg2; 

    }

    /**
     * 
     * @param value
     *  
     * @return
     */
    public static String parseConstant(String value) {
        return PREFIX + "0" + value.substring(2); 
    }


}