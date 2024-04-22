public class Processor {
    private int[] reg; 
    private int PC; 
    private int IR; 
    private Memory memory; 

    public Processor() {
        this.reg = new int[8]; 
        this.PC = 0; 
    }

    /**
     * Allocates a specific amount of memory for the Processor. 
     * 
     * @param memory
     *  The amount of memory allocated for the Processor. 
     */
    public void setMemory(Memory memory) {
        this.memory = memory; 
    }


    /**
     * Set the PC to point to the specified address. 
     * @param address
     *  The address that the program counter is pointing to. 
     */
    public void setPC(int address) {
        this.PC = address; 
    }

    /**
     * Read the instruction stored in the memory. 
     * 
     * @return
     *  Boolean value representing the Instruction stored in the cell. 
     */
    public boolean step() {
        IR = memory.read(PC++); 
        return IR == 0; 
    }

    /**
     * Dump the Processor information - Registers, PC, and IR into the Console. Values shown in the 
     * Processor information is the hexadecimal representation. 
     * 
     */
    public void dump() {
        System.out.println("Registers:");
        for (int i = 0; i < reg.length; i++) { 
            System.out.printf("Register %d = %s %n", i, Memory.getHex(reg[i]));
        }
        System.out.println("PC = " + PC);
        System.out.println("IR = " + Integer.toHexString(IR));
    }

    
}