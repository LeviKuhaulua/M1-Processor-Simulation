/**
 * The Memory class simulates the Micro-1 Computer's memory. This allows
 * for values to be stored in the computers memory (with an initial capacity set to 256). 
 * Works in tandem with the {@link Processor} where values in the computer's memory can be 
 * manipulated and stored in the registers. 
 * 
 * <p> 
 *  <b>Note</b> Values outputted in the {@link Console} are represented in Hexadecimal notation and not Decimal. 
 * </p> 
 * @author 
 *  Levi Kuhaulua
 * @version
 *  21.0.2
*/
public class Memory {
    
    private int[] cell; 
    private int cap = 256; 

    /**
     * Constructor that sets an initial capacity of 256
     */
    public Memory() {
        this.cell = new int[cap]; 
    }

    /**
     * Constructor that specifies the amount of memory to allocate
     * 
     * @param cap
     *  The amount of memory to allocate. 
     */
    public Memory(int cap) {
        this.cap = cap; 
        this.cell = new int[cap]; 
    }

    /**
     * Get the value stored in the specific memory address. 
     * 
     * @param address
     *  The address of where the value is stored. 
     * @return
     *  Integer value stored in the specified address. 
     */
    public int read(int address) {
        return cell[address]; 
    }

    /**
     * Write the value to the memory address. 
     * @param address
     *  Memory address location that you want to store the data in. 
     * @param data
     *  Value that you want to put in the address. 
     */
    public void write(int address, int data) {
        cell[address] = data; 
    }

    /**
     * Dump the memory contents into the console. When it dumps the memory contents, it will translate them into the 
     * into the hexadecimal representation of the values.
     * 
     */
    public String[] dump() {
        String[] cellContents = new String[cell.length]; 
        for (int i = 0; i < cell.length; i++) {
            cellContents[i] = "Cell[" + getHex(i) + "] = " + getHex(cell[i]); 
        }
        return cellContents; 
    }

    /**
     * Converts a decimal value to it's Hex representation. 
     * 
     * @param decimal
     *  The decimal value that you want to represent in Hexadecimal 
     * @return
     *  A string value that is the Hex representation of the Decimal value. 
     */
    protected static String getHex(int decimal) {
        return Integer.toHexString(decimal); 
    }
}