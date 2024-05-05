import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 


/**
 * GUI implementation to replace the {
 *
 * @see Console} interface.
 * @author Noeleen Silva
 */
public class Micro1Viewer extends JFrame {

    private JTextField[] registerFields;
    private Processor cpu; 
    private Memory memory; 
    private boolean fileLoaded = false; 
    private final String EXT = "m1"; 

    public Micro1Viewer() {
        // Setting Frame Up. 
        super("Micro1 Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        // Setting up fields for Registers and Memory
        registerFields = new JTextField[8];
        for(int i = 0; i < registerFields.length;i++){
            registerFields[i] = new JTextField(10);
            registerFields[i].setEditable(false);
        }
        JTextField cpuField = new JTextField(10); 
        cpuField.setEditable(false); 
        JTextField irField = new JTextField(10); 
        irField.setEditable(false);
        JTextArea memoryArea = new JTextArea(10, 10); 
        memoryArea.setLineWrap(true); 
        memoryArea.setEditable(false); 
        JScrollPane memoryScroll = new JScrollPane(memoryArea, 
                                                   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                                                   JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        
        // Variables needed to get the register and memory information. 
        memory = new Memory(256); 
        cpu = new Processor(); 
        cpu.setMemory(memory); 

        // Create buttons for console commands
        JButton stepButton = new JButton("Step");
        JButton loadButton = new JButton("Load");
        JButton memoryButton = new JButton("Memory");
        JButton registersButton = new JButton("Registers");

    
        stepButton.addActionListener((ActionEvent e) -> {
            // Step through the file or load one if it wasn't previously loaded. 
            if (!fileLoaded) {
                loadButton.doClick(); 
           } else {
                try {
                    int num = Integer.parseInt(JOptionPane.showInputDialog("Enter Step Number:")); 
                    if (num <= 0) {
                        throw new IllegalArgumentException("Invalid Number. Must be greater than 0."); 
                    }
                    boolean halt = false; 
                    for (int i = 0; i < num && !halt; i++) { 
                        if (!halt) {
                            halt = cpu.step(); 
                        }

                        if (halt) {
                            JOptionPane.showMessageDialog(this, "Program terminated"); 
                            break; 
                        }
                    }
                } catch (NumberFormatException x) {
                        // When it cannot translate the String to a number. 
                        JOptionPane.showMessageDialog(this, "Error: " + x.getMessage()); 
                } catch (IllegalArgumentException x) {
                        // When a user puts a number that is <= 0. 
                        JOptionPane.showMessageDialog(this, x.getMessage()); 
                }
            }
           
        });

        loadButton.addActionListener((ActionEvent e) -> {
          // Handles getting the file that the user chooses. 
          JFileChooser chooser = new JFileChooser("C:\\"); 
          // Only allow asm or m1 files. 
          chooser.setFileFilter(new FileNameExtensionFilter("M1 Processor Files", "asm", "m1")); 
          int result = chooser.showOpenDialog(null); 
          File fileToCompile = null; 
          File translatedFile = null; 
          Scanner fileToRead = null; 
          // Start compiling the file when user chooses it. 
          if (result == JFileChooser.APPROVE_OPTION) {
            fileToCompile = chooser.getSelectedFile(); 
            translatedFile = new File(fileToCompile.getName().replace("asm", EXT)); 
            try {
                fileToRead = new Scanner(fileToCompile); 
                boolean haltEncountered = false; 
                int address = 0; 
                String[] argLine = null; 
                String translation = null; 
                cpu.setPC(0); 
                if (Assembler.checkFile(fileToCompile)) {
                    while (fileToRead.hasNext()) {
                        argLine = fileToRead.nextLine().split("[ ]+"); 
                        if (argLine.length == 0) {
                            // Skip empty lines. 
                            continue; 
                        } else if (argLine.length == 1 && argLine[0].equalsIgnoreCase("halt")) {
                            // Halts are all 0s
                            translation = "00000000"; 
                            haltEncountered = true; 
                        } else if (haltEncountered) {
                            // Continously translate and then write to memory and m1 file until we reach end of file. 
                            while (fileToRead.hasNext()) { 
                                translation = fileToRead.nextLine(); 
    
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
                       
                        // End off with writing the translated assembly code into the memory and file. 
                        Assembler.writeToFile(translatedFile, translation + "\n"); 
                        memory.write(address++, Integer.parseInt(translation, 16)); 
                    }
                    
                } else {
                    // If file chosen is already in hexadecimal format. 
                    while (fileToRead.hasNext()) {
                        memory.write(address++, fileToRead.nextInt(16)); 
                    }
                }
                
                fileToRead.close(); 
                JOptionPane.showMessageDialog(this, "File successfully loaded");
                // Show the register and memory content after loading file. 
                fileLoaded = true; 
              } catch (FileNotFoundException x) { 
                JOptionPane.showMessageDialog(null, x.getMessage()); 
              } 
              registersButton.doClick(); 
              memoryButton.doClick(); 
          } else {
            // Do nothing as this means that user did not select file (closed out or canceled)
          }

          

        });

        memoryButton.addActionListener((ActionEvent e) -> {
            // Load the memory contents or load the file if it wasn't loaded yet. 
            if (!fileLoaded) {
                loadButton.doClick(); 
            } else {
                String[] cellContent = memory.dump(); 
                for (int i = 0; i < cellContent.length; i++) {
                    memoryArea.append(cellContent[i] + "\n"); 
                }
                memoryArea.setCaretPosition(0); // Set it to the top of the page. 
            }
            
        });

        registersButton.addActionListener((ActionEvent e) -> {
            // Load the register contents or load the file if not loaded yet. 
           if (!fileLoaded) {
              loadButton.doClick(); 
           } else {
              String[] registerContent = cpu.dump(); 
              // Register Content
              for (int i = 0; i < 8; i++) {
                registerFields[i].setText(registerContent[i]); 
                }

              cpuField.setText(registerContent[8]); 
              irField.setText(registerContent[9]); 
            }
           
        });

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stepButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(memoryButton);
        buttonPanel.add(registersButton);

        // Create panel for register fields
        JPanel registerPanel = new JPanel(new GridLayout(0, 2));
        for(int i =0; i < registerFields.length;i++){
            registerPanel.add(new JLabel("reg["+i+"]:"));
            registerPanel.add(registerFields[i]);
        }
        registerPanel.add(cpuField); 
        registerPanel.add(irField); 

        // Add panels to the main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(registerPanel, BorderLayout.CENTER);
        getContentPane().add(memoryScroll, BorderLayout.SOUTH); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Micro1Viewer().setVisible(true);
        });
    }
}
