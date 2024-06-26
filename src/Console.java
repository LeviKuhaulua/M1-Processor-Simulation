import java.util.*;
import java.io.*;

/**
  Instances of this class represent console user interfaces to a
  simulated computer equipped with a Micro-1 processor.
  */
public class Console {
    /**
      Representation of the keyboard
    */
	private Scanner kbd = new Scanner(System.in);
	/**
	  Main memory of the simulated computer
	*/
	private Memory memory;
	/**
	  Processor of the simulated computer
	*/
	private Processor cpu;

    /**
       Constructs a memory with specified number of cells,
       and constructs an associated processor.

       @param cap  the sepcified amount of memory
    */
	public Console(int cap) {
		memory = new Memory(cap);
		cpu = new Processor();
		cpu.setMemory(memory);
	}

    /**
      Constructs a processor and a memory with 256 cells
    */
	public Console() {
		this(256);
	}

    /**
      Loads hexadecimal numbers stored in fName into
      memory starting at address 0. Resets PC to 0.

      @param fName the name of a file containing hex numbers
    */
	public void load(String fName) {
		try {
			boolean haltEncountered = false; 
			File f = new File(fName);
			Scanner scan  = new Scanner(f);
			int address = 0;
			String[] argLine = null; 
			String translation = null; 
			cpu.setPC(0);
			if (Assembler.checkFile(f)) {
				while (scan.hasNext()) {
					argLine = scan.nextLine().split("[ ]+"); 
					// System.out.println(Arrays.toString(argLine));
					if (argLine.length == 1 && argLine[0].equalsIgnoreCase("halt")) {
						translation = "00000000"; 
						haltEncountered = true;  
					} else if (haltEncountered) {
						// Treat as hexadecimal constants after the first halt statement is encountered. 
						while (scan.hasNext()) {
							translation = scan.nextLine(); 
							// Handles case where another halt statement is encountered. 
							if (translation.equalsIgnoreCase("halt")) {
								memory.write(address++, 0); 
							} else {
								memory.write(address++, Integer.parseInt(translation, 16));  
							}							
						}
						break; 
					} else if (argLine.length == 3) {
						translation = Assembler.parseArguments(Assembler.parseCommand(argLine[0]), argLine[1], argLine[2]); 
					} else if (argLine.length == 1) {
						translation = Assembler.parseConstant(argLine[0]); 
					} else {
						translation = Assembler.parseArguments(Assembler.parseCommand(argLine[0]), argLine[1]);
					}

					memory.write(address++, Integer.parseInt(translation, 16)); 

				}
			} else {
				while(scan.hasNext()) {
					memory.write(address++, scan.nextInt(16));
				}
			}
			
			scan.close(); 
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

    /**
      Displays synopsis of all commands in the console window
    */
	public void help() {
		System.out.println("load fileName \t loads hex memory image into memory");
		System.out.println("memory \t\t dumps memory to console");
		System.out.println("registers \t dumps registers to console");
		System.out.println("step N \t\t executes next N instructions or until halt");
		System.out.println("help \t\t displays this message");
		System.out.println("quit \t\t terminate console");
	}
    /**
      This is the read-execute-print loop for the console. It perpetually

         1) displays a prompt
         2) reads a command from the keyboard
         3) executes the command
         4) displays the result
      Commands include quit, help, load (a program from a file),
      memory (display contents of memory), registers (display
      contents of registers), and step N (execute the next N
      instructions.
    */
	public void controlLoop() {
		System.out.println("type \"help\" for commands");
		while(true) {
			System.out.print("-> ");
			String cmmd = kbd.next();
			if (cmmd.equals("quit")) {
				break;
			} else if (cmmd.equals("help")) {
				help();
			} else if (cmmd.equals("load")) {
				load(kbd.next());
				System.out.println("done");
			} else if (cmmd.equals("memory")) {
				memory.dump();
			} else if (cmmd.equals("registers")) {
				cpu.dump();
			} else if (cmmd.equals("step")) {
				int num;
				if (!kbd.hasNextInt()) {
					num = 0;
					kbd.nextLine();
					System.out.println("invalid number of steps");
				} else {
					num = kbd.nextInt();
					boolean halt = false;
					for(int i = 0; i < num && !halt; i++) {
						if (!halt) halt = cpu.step();
						if (halt) {
							System.out.println("program terminated");
							break;
						}
					}
					System.out.println("done");
			} } else {
				System.out.println("unrecognized command: " + cmmd);
				if (kbd.hasNext()) kbd.nextLine();
			}
		}
		System.out.println("bye");
	}

    /**
    Creates a console (with memory and CPU), then starts the
    console's control loop.
    */
	public static void main(String[] args) {
		Console console = new Console();
		console.controlLoop();
	}

}
