import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter; 
import java.util.Scanner;

import javax.swing.JOptionPane; 
/**
* Static class that handles converting Assembly language to Machine (hex) language. 
* @author
*  Levi Kuhaulua
* @version 
*  21.0.2
*/
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
     *  The hexadecimal representation of the Assembly command and arguments. 
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
     * Will take in a constant value and return the Hexadecimal representation of the constant. 
     * @param value
     *  The constant value to be parsed. 
     * @return
     *  The hexadecimal representation of the constant value. 
     */
    public static String parseConstant(String value) {
        return PREFIX + "0" + value.substring(2); 
    }

    /**
     * Will take in the translated command (from Assembly to Hexadecimal), then write them to a file. 
     * @param file
     *  File where translations will be written to
     * @param content
     *  Translation to write to the file. 
     */
    public static void writeToFile(File file, String content) {
        File f = file; 
        try (FileWriter out = new FileWriter(f, true)) {
            out.write(content); 
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage()); 
        }
    }

}