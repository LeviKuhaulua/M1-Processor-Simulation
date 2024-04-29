import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI implementation to replace the {@see Console} interface. 
 * @author
 *  Noeleen Silva
 */
public class Micro1Viewer extends JFrame {
    private JTextField registerField1;
    private JTextField registerField2;
    // Add more text fields for other registers as needed

    public Micro1Viewer() {
        super("Micro1 Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        // Create text fields for registers
        registerField1 = new JTextField(10);
        registerField1.setEditable(false);
        registerField2 = new JTextField(10);
        registerField2.setEditable(false);
        // Add more text fields for other registers as needed

        // Create buttons for console commands
        JButton stepButton = new JButton("Step");
        JButton loadButton = new JButton("Load");
        JButton memoryButton = new JButton("Memory");
        JButton registersButton = new JButton("Registers");

        // Add action listeners to buttons
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement step action here
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement load action here
            }
        });

        memoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement memory action here
            }
        });

        registersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update text fields with register content
                // For example:
                registerField1.setText("Content of Register 1");
                registerField2.setText("Content of Register 2");
                // Update other register fields as needed
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
        registerPanel.add(new JLabel("Register 1:"));
        registerPanel.add(registerField1);
        registerPanel.add(new JLabel("Register 2:"));
        registerPanel.add(registerField2);
        // Add more labels and text fields for other registers as needed

        // Add panels to the main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(registerPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Micro1Viewer().setVisible(true);
            }
        });
    }
}