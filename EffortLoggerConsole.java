package EffortLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;  // added by Jana for Planning Poker
import java.nio.file.*;
import java.io.IOException;



/**
 * The EffortLoggerConsole class represents the main application for the Effort Logger Console.
 */
public class EffortLoggerConsole {

    private static JFrame frame; // Main frame
    private static JPanel loginPanel; // Panel for login
    private static JPanel mainPanel; // Main application panel

    private static JPanel clockPanel; // Panel for clock status
    private static JLabel clockLabel; // Label for clock status
    private static JComboBox<String> projectDropdown; // Dropdown for project selection
    private static JComboBox<String> lifeCycleDropdown; // Dropdown for life cycle selection
    private static JComboBox<String> effortCategoryDropdown; // Dropdown for effort category selection
    private static JComboBox<String> deliverableDropdown; // Dropdown for deliverable selection

    private static JTextField usernameField; // Username field
    private static JPasswordField passwordField; // Password field

    private static EffortLogger effortLogger = new EffortLogger(); // Create an instance of EffortLogger
    private static PlanningPoker planningPoker = new PlanningPoker();
    private static EffortLoggerHelp effortLoggerHelp = new EffortLoggerHelp();

    public static void main(String[] args) throws IOException{
    	//EncryptDecrypt.encryptFile("ZackBeckwith.txt", "user_info_encrypted", true); //USED FOR TESTING
    	SwingUtilities.invokeLater(() -> createAndShowGUI());
    	//EncryptDecrypt.decryptFile("user_info_encrypted", "ZackBeckwith_encrypted.txt"); //USED FOR TESTING
    }

    /**
     * Create and display the main GUI.
     */
    private static void createAndShowGUI() {
        
    	frame = new JFrame("Effort Logger Console");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300); // Initial frame size
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Create the login panel and add it to the frame initially
        createLoginPanel();
        frame.add(loginPanel);
        frame.setVisible(true);
    }

    /**
     * Create the login panel.
     */
    private static void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        JPanel loginFields = new JPanel();
        loginFields.setLayout(new BoxLayout(loginFields, BoxLayout.Y_AXIS));

        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set field size
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30)); // Set field size

        JButton loginButton = new JButton("Login");
        
      //turtorial button(hoang)
        JButton HelpButton = new JButton("Help");
        HelpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        HelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turtorial_login(loginFields, 0);
            } 
        });
        //
        
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validate the username and password here (replace with your validation logic)
                try
                {
                	if (isValidLogin(username, password)) 
                	{
                        frame.remove(loginPanel); // Remove the login panel
                        createMainPanel(); // Create and show the main panel
                        frame.setSize(1200, 800); // Adjusted frame size after login
                        frame.setLocationRelativeTo(null); // Center the frame
                	}
                	else
                	{
                		JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                        // You can add more error handling here
                	}
                }
                catch(Exception e1)
                {
                	e1.printStackTrace();
                }
            }
        });
     

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginFields.add(usernameLabel);
        loginFields.add(usernameField);
        loginFields.add(passwordLabel);
        loginFields.add(passwordField);
        loginFields.add(loginButton);
        
      //turtorial_login (hoang)
        loginFields.add(HelpButton);
        //

        loginPanel.add(loginFields, BorderLayout.CENTER);
    }

    /**
     * Validate the login credentials.
     *
     * @param username The entered username
     * @param password The entered password
     * @return true if the login is valid, false otherwise
     */
    private static boolean isValidLogin(String username, String password) throws IOException
    {
        Path validUser = EncryptDecrypt.searchUserFiles(username, password);
    	if(validUser == null)
    	{
    		return false;
    	}
    	else
    	{
    		EffortLogger.setUserInfo(validUser);
    		return true;
    	}
    }

    /**
     * Create the main application panel.
     */
    private static void createMainPanel() {
        mainPanel = new JPanel(new GridLayout(0, 1));
        // Add existing components to the mainPanel
        titlePanel(mainPanel);
        clockPanel(mainPanel);
        startActivityPanel(mainPanel);
        projectInfoPanel(mainPanel);
        stopActivityPanel(mainPanel);

                                        // CHANGES MADE BY JANA

        JButton startPlanningPokerButton = new JButton("Start Planning Poker");
        startPlanningPokerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Integer> estimates = planningPoker.startPlanningPoker();
        }
    });

    // Add the Planning Poker button to GUI
    mainPanel.add(startPlanningPokerButton);

                                            // CHANGES BY JANA END HERE


        frame.add(mainPanel); // Add the mainPanel to the frame
        frame.setVisible(true); // Make the mainPanel visible
    }

    /**
     * Adds a title panel to the parent panel at the top of the GUI.
     * @param parentPanel The parent panel to which the title panel will be added.
     */
    private static void titlePanel(JPanel parentPanel) {
        // Create a new panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());

        // Create a label with the title text
        JLabel titleLabel = new JLabel("Effort Logger Console");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the title label to the title panel
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Add the title panel to the parent panel
        parentPanel.add(titlePanel);
        
     // turtorial for the poker(hoang)
        JButton HelpButton = new JButton("Help");
        titlePanel.add(HelpButton, BorderLayout.WEST);
        HelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turtorial_login(parentPanel, 1);
            } 
        });
        
    }

    /**
     * Creates and sets up the clock panel for displaying the clock status.
     * @param parentPanel The parent panel to which the clock panel will be added.
     */
    private static void clockPanel(JPanel parentPanel) {
        // Create a panel for the clock
        clockPanel = new JPanel();
        clockPanel.setBackground(Color.RED);
        clockPanel.setPreferredSize(new Dimension(parentPanel.getWidth(), 50));

        // Create a label for the clock status
        clockLabel = new JLabel("Clock is Stopped");
        clockLabel.setFont(new Font("Arial", Font.BOLD, 14));
        clockLabel.setForeground(Color.WHITE);

        // Add the clock label to the clock panel
        clockPanel.add(clockLabel);

        // Add the clock panel to the parent panel
        parentPanel.add(clockPanel);
    }

    /**
     * Creates and sets up the "Start Activity" panel with a button for starting a new activity.
     * @param parentPanel The parent panel to which the "Start Activity" panel will be added.
     */
    private static void startActivityPanel(JPanel parentPanel) {
        // Create a panel for starting an activity
        JPanel startActivityPanel = new JPanel(new BorderLayout());

        // Create a label with instructions for starting an activity
        JLabel startInstructionLabel = new JLabel("1. When you start a new activity, press the \"Start an Activity\" button.");
        startInstructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        startInstructionLabel.setForeground(Color.BLACK);

        // Create a button for starting the activity
        JButton startButton = new JButton("Start an Activity");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any of the dropdowns have empty selections
                if (projectDropdown.getSelectedItem() == "" || lifeCycleDropdown.getSelectedItem() == "" || effortCategoryDropdown.getSelectedItem() == "" || deliverableDropdown.getSelectedItem() == "") {
                    // Display an error message
                    JOptionPane.showMessageDialog(null, "Please make sure a project, life cycle, effort category, and deliverable are selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    // Update the clock status to running
                    clockPanel.setBackground(Color.GREEN);
                    clockLabel.setText("Clock is Running");

                    // Assuming EffortLogger is a class
                    // EffortLogger effortLogger = new EffortLogger(); // Create an instance of EffortLogger

                    String lifeCycle = lifeCycleDropdown.getSelectedItem().toString();
                    String effortCategory = effortCategoryDropdown.getSelectedItem().toString();

                    // Call the non-static method startClock on the instance
                    effortLogger.startClock(lifeCycle, effortCategory);
                }
            }
        });

        // Add the instruction label to the "Start Activity" panel
        startActivityPanel.add(startInstructionLabel, BorderLayout.NORTH);

        // Add the start button to the "Start Activity" panel
        startActivityPanel.add(startButton, BorderLayout.CENTER);

        // Add the "Start Activity" panel to the parent panel
        parentPanel.add(startActivityPanel);
    }

    /**
     * Creates and sets up the "Project Information" panel for selecting project details.
     * @param parentPanel The parent panel to which the "Project Information" panel will be added.
     */
    private static void projectInfoPanel(JPanel parentPanel) {
        // Create a panel for project information
        JPanel projectInfoPanel = new JPanel(new BorderLayout());

        // Create a label with instructions for selecting project details
        JLabel projectInfoLabel = new JLabel("2. Select the project, life cycle, effort category, and deliverable from the following lists:");
        projectInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        projectInfoLabel.setForeground(Color.BLACK);

        // Create dropdown lists for project options, life cycle options, effort category options, and deliverable options
        projectDropdown = new JComboBox<>(new String[]{"", "Business Project", "Development Project"});
        lifeCycleDropdown = new JComboBox<>(new String[]{"", "Information Gathering", "Information Understanding", "Verifying", "Outlining", "Drafting", "Finalizing", "Team Meeting", "Coach Meeting", "Stakeholder Meeting"});
        effortCategoryDropdown = new JComboBox<>(new String[]{"", "Deliverables", "Interruptions", "Defects", "Other"});
        deliverableDropdown = new JComboBox<>(new String[]{"", "Risk Management Plan", "Conceptual Design Plan", "Detailed Design Plan", "Implementation Plan"});

        // Create a panel to organize the dropdown lists
        JPanel dropdownPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column

        // Create a panel for the top row of dropdowns (Project and Life Cycle)
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topRow.add(new JLabel("Project: "));
        topRow.add(projectDropdown);
        topRow.add(new JLabel("Life Cycle: "));
        topRow.add(lifeCycleDropdown);

        // Create a panel for the bottom row of dropdowns (Effort Category and Deliverable)
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomRow.add(new JLabel("Effort Category: "));
        bottomRow.add(effortCategoryDropdown);
        bottomRow.add(new JLabel("Deliverable: "));
        bottomRow.add(deliverableDropdown);

        // Add the top and bottom rows to the dropdown panel
        dropdownPanel.add(topRow);
        dropdownPanel.add(bottomRow);

        // Add the project information label to the "Project Information" panel
        projectInfoPanel.add(projectInfoLabel, BorderLayout.NORTH);

        // Add the dropdown panel to the "Project Information" panel
        projectInfoPanel.add(dropdownPanel, BorderLayout.CENTER);

        // Add the "Project Information" panel to the parent panel
        parentPanel.add(projectInfoPanel);
    }

    /**
     * Creates and sets up the "Stop Activity" panel with a button for stopping the current activity.
     * @param parentPanel The parent panel to which the "Stop Activity" panel will be added.
     */
    private static void stopActivityPanel(JPanel parentPanel) {
        // Create a panel for stopping the activity
        JPanel stopActivityPanel = new JPanel(new BorderLayout());

        // Create a label with instructions for stopping the activity
        JLabel stopInstructionLabel = new JLabel("3. Press the \"Stop this Activity\" to generate an effort log entry using the attributes above.");
        stopInstructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        stopInstructionLabel.setForeground(Color.BLACK);

        // Create a button for stopping the activity
        JButton stopButton = new JButton("Stop this Activity");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the clock status to stopped
                clockPanel.setBackground(Color.RED);
                clockLabel.setText("Clock is Stopped");

                // Assuming EffortLogger is a class
                // EffortLogger effortLogger = new EffortLogger(); // Create an instance of EffortLogger

                // Stop the clock (placeholder for actual implementation)
                effortLogger.endClock();
            }
        });

        // Add the instruction label to the "Stop Activity" panel
        stopActivityPanel.add(stopInstructionLabel, BorderLayout.NORTH);

        // Add the stop button to the "Stop Activity" panel
        stopActivityPanel.add(stopButton, BorderLayout.CENTER);

        // Add the "Stop Activity" panel to the parent panel
        parentPanel.add(stopActivityPanel);
    }

//turtorial class (hoang)
private static void turtorial_login(JPanel parentPanel, int state){
    effortLoggerHelp.GifExample(state);
}
}
