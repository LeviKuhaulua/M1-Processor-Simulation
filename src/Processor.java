/**
 * Class to simulate processor of Micro-1 chip. This manipulates the values 
 * stored in the {@link Memory} class and assigns them to the appropriate
 * register. 
 * 
 * <p>
 * <b>Note</b> that values outputted in the {@link Console} is in 
 * Hexadecimal notation and not Decimal.
 * </p> 
 * 
 * @author
 *  <a href="mailto:levi.kuhaulua@outlook.com">Levi Kuhaulua</a> 
 * @version
 *  21.0.2
 */
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
     * Read the instruction stored in the memory and evaluate. It will stop when it encounters the first halt statement or an unknown command. 
     * 
     * @return
     *  Boolean value representing if the halt command or unknown command was reached.  
     * @throws
     *  ArithmeticException should there be a divison by 0. 
     */
    public boolean step() {
        IR = memory.read(PC++); // Get the address of the instruction set. 
        int command = (IR & 0b111100000000) >> 8; 
        int arg1 = (IR & 0b000011110000) >> 4;
        int arg2 = (IR & 0b000000001111);
        switch (command) {
            case 0: 
                // Halt program. Does not have to deal with possibility that this is to load a value into a register. 
                // HANDLED BY case 2 block. 
                return true; 
            case 1: 
                // load a b = reg[a] = cell[reg[b]]
                reg[arg1] = memory.read(reg[arg2]); 
                return false;  
            case 2: 
                // loadc a
                reg[arg1] = (IR = memory.read(PC++)); 
                // Goes back to the next logical unit. 
                return false; 
            case 3: 
                // store a b = cell[reg[a]] = reg[b] 
                memory.write(reg[arg1], reg[arg2]);  
                return false; 
            case 4: 
                // add a b = reg[a] = reg[a] + reg[b]
                reg[arg1] += reg[arg2]; 
                return false; 
            case 5: 
                // mul a b = reg[a] = reg[a] * reg[b]
                reg[arg1] *= reg[arg2]; 
                return false; 
            case 6:
                // sub a b = reg[a] = reg[a] - reg[b]
                reg[arg1] -= reg[arg2]; 
                return false;
            case 7: 
                // div a b = reg[a] = reg[a] - reg[b]; 
                try {
                    reg[arg1] /= reg[arg2]; 
                } catch (ArithmeticException e) { 
                    System.out.println("Cannot divide by 0. ");
                }
                return false; 
            case 8: 
                // and a b 
                if (reg[arg1] != 0 && reg[arg2] != 0) {
                    reg[arg1] = 1;
                } else {
                    reg[arg1] = 0; 
                }
                return false; 
            case 9: 
                // or a b
                if (reg[arg1] != 0 || reg[arg2] != 0) {
                    reg[arg1] = 1; 
                } else {
                    reg[arg1] = 0; 
                }
                return false; 
            case 10: 
                // not a b 
                if (reg[arg2] != 0) {
                    reg[arg1] = 0; 
                } else {
                    reg[arg1] = 1; 
                }
                return false; 
            case 11: 
                // lshift a b = reg[a] = reg[b] << 1
                reg[arg1] = reg[arg2] << 1; 
                return false; 
            case 12: 
                // rshift a b = reg[a] = reg[b] >> 1
                reg[arg1] = reg[arg2] >> 1; 
                return false; 
            case 13: 
                // bwc a b = reg[a] = reg[a] & reg[b]
                reg[arg1] = reg[arg1] & reg[arg2]; 
                return false; 
            case 14: 
                // bwd a b = reg[a] = reg[a] | reg[b]
                reg[arg1] = reg[arg1] | reg[arg2]; 
                return false; 
            case 15: 
                // if a b = if (reg[a] != 0) pc = reg[b]
                if (reg[arg1] != 0) {
                    PC = reg[arg2]; 
                }
                return false; 
            default: 
                System.out.println("Unrecognized command: " + command);
                return true; 
        }
    }

    /**
     * Dump the Processor information - Registers, PC, and IR into the Console. Values shown in the 
     * Processor information is the hexadecimal representation except for the PC. 
     */
    public void dump() {
        System.out.println("Registers:");
        for (int i = 0; i < reg.length; i++) { 
            System.out.printf("Register %s = %s %n", i, Memory.getHex(reg[i])); 
        }
        System.out.println("PC = " + Memory.getHex(PC));
        System.out.println("IR = " + Integer.toHexString(IR));
    }

        
}