package EffortLogger;

//
//File Name: EffortLoggerConsole.java
//Creator: Andrew Knoll
//Last Updated: 11/28/2023
//Project: CSE360 Team Th15 EFFORTLOGGER V2
//Description: The EffortLoggerConsole class represents the main application for the Effort Logger Console.
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.*;
import java.io.File;
import java.io.IOException;


public class EffortLoggerConsole {

    // GUI components declaration
    public static JFrame frame; // Main frame of the application
    private static JPanel loginPanel; // Panel for user login
    private static JPanel mainPanel; // Main panel displayed after login

    // Components for clock functionality
    private static JPanel clockPanel; // Panel displaying the clock status
    private static JLabel clockLabel; // Label showing the current status of the clock
    private static Timer clockTimer; // Timer for updating the clock
    private static long startTime; // Stores the start time for the timer
    private static JLabel timerLabel; // Label for displaying the elapsed time

    // Dropdown menus for user selection
    private static JComboBox<String> projectDropdown; // Dropdown to select a project
    private static JComboBox<String> lifeCycleDropdown; // Dropdown to select a life cycle phase
    private static JComboBox<String> effortCategoryDropdown; // Dropdown to select an effort category
    private static JComboBox<String> deliverableDropdown; // Dropdown to select a deliverable

    // Fields for user credentials
    private static JTextField usernameField; // Field to enter username
    private static JPasswordField passwordField; // Field to enter password

    // Instances of helper classes for additional functionalities
    private static EffortLogger effortLogger = new EffortLogger(); // Instance for logging efforts
    private static PlanningPoker planningPoker = new PlanningPoker(); // Instance for planning poker functionality

    /**
     * The main method to start the application.
     *
     * @param args Command line arguments (not used here).
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        // Ensures the GUI is created in the Event Dispatch Thread for thread safety.
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    /**
     * Initializes and displays the main GUI of the application.
     */
    private static void createAndShowGUI() {
        frame = new JFrame("Effort Logger Console"); // Initialize the main application window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        frame.setSize(400, 300); // Set the initial size of the frame
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        createLoginPanel(); // Create the login panel
        frame.add(loginPanel); // Add the login panel to the frame
        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Creates the login panel with username and password fields and a login button.
     */
    private static void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout()); // Use BorderLayout for layout
        JPanel loginFields = new JPanel(); // Panel for the login fields
        loginFields.setLayout(new BoxLayout(loginFields, BoxLayout.Y_AXIS)); // Vertical box layout for the fields

        // Initialize username and password fields
        usernameField = new JTextField(20); // Set field width
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30)); // Set preferred size

        JButton loginButton = new JButton("Login"); // Button for logging in

        // ActionListener for handling login action
        ActionListener loginActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin(); // Call method to handle login
            }
        };

        // Attach the action listener to username and password fields
        usernameField.addActionListener(loginActionListener);
        passwordField.addActionListener(loginActionListener);

        // Create Help button for tutorial access
        JButton HelpButton = new JButton("Help");
        HelpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        HelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turtorial_login(loginFields, 0); // Trigger the tutorial
            } 
        });
        
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the login button
        loginButton.addActionListener(loginActionListener); // Attach action listener to login button

        // Labels for username and password
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to the login fields panel
        loginFields.add(usernameLabel);
        loginFields.add(usernameField);
        loginFields.add(passwordLabel);
        loginFields.add(passwordField);
        loginFields.add(loginButton);
        loginFields.add(HelpButton); // Add the help button

        loginPanel.add(loginFields, BorderLayout.CENTER); // Add login fields to the login panel
    }

    /**
     * Performs user login when the login button is pressed.
     * Validates the entered username and password.
     */
    private static void performLogin() {
        String username = usernameField.getText(); // Get entered username
        String password = new String(passwordField.getPassword()); // Get entered password

        try {
            // Check if the username and password are valid
            if (isValidLogin(username, password)) {
                frame.remove(loginPanel); // Remove the login panel from the frame
                createMainPanel(); // Create and display the main panel
                frame.setSize(1200, 800); // Resize the frame after login
                frame.setLocationRelativeTo(null); // Re-center the frame
            } else {
                // Display error message if login is invalid
                JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                // Additional error handling can be added here
            }
        } catch(Exception e) {
            e.printStackTrace(); // Print stack trace in case of exception
        }
    }

    /**
     * Validates the login credentials of the user.
     *
     * @param username The entered username
     * @param password The entered password
     * @return true if the login is valid, false otherwise
     * @throws IOException If an I/O error occurs during file operations
     */
    private static boolean isValidLogin(String username, String password) throws IOException {
        // Search for user files based on the provided credentials
        Path validUser = EncryptDecrypt.searchUserFiles(username, password);

        // Construct the path for the directory where user information is stored
        Path encryptedDirPath = Paths.get(System.getProperty("user.dir"), "user_info_encrypted");

        // Check if the directory exists, create it if it doesn't
        if (!Files.exists(encryptedDirPath)) {
            Files.createDirectories(encryptedDirPath);
        }

        // Check if a valid user file is found
        if (validUser == null) {
            // Handle default login credentials for testing purposes
            if (username.equals("user") && password.equals("password")) {
                Path testUserPath = encryptedDirPath.resolve("TestUser_encrypted.txt");
                File testUserFile = testUserPath.toFile();

                // Create and encrypt a temporary file if the test user file doesn't exist
                if (!testUserFile.exists()) {
                    File tempFile = EffortLogger.createTempUserFile("TestUser");
                    EncryptDecrypt.encryptFile(tempFile.getAbsolutePath(), encryptedDirPath.toString(), true);
                    tempFile.delete();
                }

                // Set user information in the EffortLogger
                EffortLogger.setUserInfo(testUserPath);
                return true;
            } else {
                return false;
            }
        } else {
            // Set user information if the user is found in the encrypted files
            EffortLogger.setUserInfo(validUser);
            return true;
        }
    }

    /**
     * Creates the main application panel.
     */
    private static void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());

        // Create a sub-panel for organizing the main content in the center
        JPanel centerPanel = new JPanel(new GridLayout(0, 1));
        // Add various sub-panels to the center panel
        titlePanel(centerPanel);
        clockPanel(centerPanel);
        startActivityPanel(centerPanel);
        projectInfoPanel(centerPanel);
        stopActivityPanel(centerPanel);

        // Create and add the "Start Planning Poker" button
        JButton startPlanningPokerButton = new JButton("Start Planning Poker");
        startPlanningPokerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                planningPoker = new PlanningPoker();
                planningPoker.setVisible(true);
                frame.setState(Frame.ICONIFIED); // Minimize the main frame
            }
        });
        centerPanel.add(startPlanningPokerButton); // Add the button to the center panel

        mainPanel.add(centerPanel, BorderLayout.CENTER); // Add the center panel to the main panel

        frame.add(mainPanel); // Add the main panel to the frame
        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Displays the user's data in a dialog box.
     */
    private static void viewUserData() {
        if (EffortLogger.getUserFile() != null) {
            try {
                // Read encrypted data from the user file
                String encryptedData = Files.readString(EffortLogger.getUserFile());
                // Decrypt the data
                String decryptedData = EncryptDecrypt.decryptString(encryptedData);

                // Display the decrypted data in a text area within a scroll pane
                JTextArea textArea = new JTextArea(decryptedData);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(frame, scrollPane, "User Data", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error retrieving user data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Display a message if no user data is available
            JOptionPane.showMessageDialog(frame, "No user data available.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Adds a title panel to the parent panel at the top of the GUI.
     * 
     * @param parentPanel The parent panel to which the title panel will be added.
     */
    private static void titlePanel(JPanel parentPanel) {
        JPanel titlePanel = new JPanel(new BorderLayout());

        // Create and configure the title label
        JLabel titleLabel = new JLabel("Effort Logger Console");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create and configure the Help button
        JButton helpButton = new JButton("Help");
        helpButton.setPreferredSize(new Dimension(helpButton.getPreferredSize().width, 40)); // Adjust button height
        helpButton.addActionListener(e -> turtorial_login(parentPanel, 1));

        // Create and configure the "View My Data" button
        JButton viewUserDataButton = new JButton("View My Data");
        viewUserDataButton.setPreferredSize(new Dimension(viewUserDataButton.getPreferredSize().width, 40)); // Adjust button height
        viewUserDataButton.addActionListener(e -> viewUserData());

        // Add components to the title panel
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(helpButton, BorderLayout.LINE_START);
        titlePanel.add(viewUserDataButton, BorderLayout.LINE_END);

        // Add the title panel to the parent panel
        parentPanel.add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Creates and sets up the clock panel for displaying the clock status.
     * This panel shows whether the clock is running and the current elapsed time.
     *
     * @param parentPanel The parent panel to which the clock panel will be added.
     */
    private static void clockPanel(JPanel parentPanel) {
        clockPanel = new JPanel();
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        clockPanel.setBackground(Color.RED); // Red background indicates clock is stopped
        clockPanel.setPreferredSize(new Dimension(parentPanel.getWidth(), 100)); // Set size of the clock panel

        clockLabel = new JLabel("Clock is Stopped");
        clockLabel.setFont(new Font("Arial", Font.BOLD, 16));
        clockLabel.setForeground(Color.BLACK);
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Timer label for displaying the current time in HH:MM:SS format
        timerLabel = new JLabel("00:00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Larger font size for better visibility
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        clockPanel.add(clockLabel);
        clockPanel.add(timerLabel);
        parentPanel.add(clockPanel);

        // Initialize the timer to update every second
        clockTimer = new Timer(1000, e -> updateClock());
    }

    /**
     * Updates the timer label with the elapsed time since the clock was started.
     */
    private static void updateClock() {
        long elapsedTime = System.currentTimeMillis() - startTime; // Calculate elapsed time
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long minutesDisplay = (elapsedSeconds % 3600) / 60;
        long hoursDisplay = elapsedSeconds / 3600;

        // Format and display the elapsed time
        String timeString = String.format("%02d:%02d:%02d", hoursDisplay, minutesDisplay, secondsDisplay);
        timerLabel.setText(timeString);
    }

    /**
     * Starts the clock timer and updates the UI to reflect the running state.
     */
    @SuppressWarnings("unused")
    private static void startClock() {
        startTime = System.currentTimeMillis(); // Capture the start time
        clockLabel.setText("Clock is Running"); // Update clock label
        clockTimer.start(); // Start the timer
        clockPanel.setBackground(Color.GREEN); // Green background indicates clock is running
    }

    /**
     * Stops the clock timer and resets the UI to its default state.
     */
    @SuppressWarnings("unused")
    private static void stopClock() {
        clockTimer.stop(); // Stop the timer
        clockPanel.setBackground(Color.RED); // Red background indicates clock is stopped
        clockLabel.setText("Clock is Stopped"); // Update clock label
        timerLabel.setText("00:00:00"); // Reset timer label
    }

    /**
     * Creates and sets up the "Start Activity" panel with a button for starting a new activity.
     * This panel allows the user to begin tracking time for a selected activity.
     *
     * @param parentPanel The parent panel to which the "Start Activity" panel will be added.
     */
    private static void startActivityPanel(JPanel parentPanel) {
        JPanel startActivityPanel = new JPanel(new BorderLayout());
        JLabel startInstructionLabel = new JLabel("1. When you start a new activity, press the \"Start an Activity\" button.");
        startInstructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        startInstructionLabel.setForeground(Color.BLACK);

        JButton startButton = new JButton("Start an Activity");
        startButton.addActionListener(e -> {
            // Validate if all dropdown selections are made before starting an activity
            if (projectDropdown.getSelectedItem().equals("") || lifeCycleDropdown.getSelectedItem().equals("") || 
                effortCategoryDropdown.getSelectedItem().equals("") || deliverableDropdown.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "Please make sure a project, life cycle, effort category, and deliverable are selected.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Update clock panel and start the timer
                clockPanel.setBackground(Color.GREEN);
                clockLabel.setText("Clock is Running");
                startTime = System.currentTimeMillis();
                clockTimer.start();

                // Retrieve selections from dropdowns
                String lifeCycle = lifeCycleDropdown.getSelectedItem().toString();
                String effortCategory = effortCategoryDropdown.getSelectedItem().toString();

                // Start clock with selected life cycle and effort category
                effortLogger.startClock(lifeCycle, effortCategory);
            }
        });

        startActivityPanel.add(startInstructionLabel, BorderLayout.NORTH);
        startActivityPanel.add(startButton, BorderLayout.CENTER);
        parentPanel.add(startActivityPanel);
    }

    /**
     * Creates and sets up the "Project Information" panel for selecting project details.
     * This panel allows users to select options related to the project they are working on.
     *
     * @param parentPanel The parent panel to which the "Project Information" panel will be added.
     */
    private static void projectInfoPanel(JPanel parentPanel) {
        JPanel projectInfoPanel = new JPanel(new BorderLayout()); // Main panel for project information

        // Label with instructions for selecting project details
        JLabel projectInfoLabel = new JLabel("2. Select the project, life cycle, effort category, and deliverable from the following lists:");
        projectInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        projectInfoLabel.setForeground(Color.BLACK);

        // Initialize dropdown lists for various project attributes
        projectDropdown = new JComboBox<>(new String[]{"", "Business Project", "Development Project"});
        lifeCycleDropdown = new JComboBox<>(new String[]{"", "Information Gathering", "Information Understanding", "Verifying", "Outlining", "Drafting", "Finalizing", "Team Meeting", "Coach Meeting", "Stakeholder Meeting"});
        effortCategoryDropdown = new JComboBox<>(new String[]{"", "Deliverables", "Interruptions", "Defects", "Other"});
        deliverableDropdown = new JComboBox<>(new String[]{"", "Risk Management Plan", "Conceptual Design Plan", "Detailed Design Plan", "Implementation Plan"});

        // Panel to organize the dropdown lists in a grid layout
        JPanel dropdownPanel = new JPanel(new GridLayout(2, 1)); // 2 rows for dropdowns

        // Panels for the top and bottom rows of dropdowns
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topRow.add(new JLabel("Project: "));
        topRow.add(projectDropdown);
        topRow.add(new JLabel("Life Cycle: "));
        topRow.add(lifeCycleDropdown);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomRow.add(new JLabel("Effort Category: "));
        bottomRow.add(effortCategoryDropdown);
        bottomRow.add(new JLabel("Deliverable: "));
        bottomRow.add(deliverableDropdown);

        dropdownPanel.add(topRow);
        dropdownPanel.add(bottomRow);

        projectInfoPanel.add(projectInfoLabel, BorderLayout.NORTH); // Add label to the panel
        projectInfoPanel.add(dropdownPanel, BorderLayout.CENTER); // Add dropdowns to the panel
        parentPanel.add(projectInfoPanel); // Add project info panel to the parent panel
    }

    /**
     * Creates and sets up the "Stop Activity" panel with a button for stopping the current activity.
     * This panel allows users to stop the time tracking for the current activity.
     *
     * @param parentPanel The parent panel to which the "Stop Activity" panel will be added.
     */
    private static void stopActivityPanel(JPanel parentPanel) {
        JPanel stopActivityPanel = new JPanel(new BorderLayout()); // Panel for stopping activity
        JLabel stopInstructionLabel = new JLabel("3. Press the \"Stop this Activity\" to generate an effort log entry using the attributes above.");
        stopInstructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        stopInstructionLabel.setForeground(Color.BLACK);

        // Button to stop the current activity
        JButton stopButton = new JButton("Stop this Activity");
        stopButton.addActionListener(e -> {
            clockPanel.setBackground(Color.RED); // Change color to indicate stopped clock
            clockLabel.setText("Clock is Stopped");
            clockTimer.stop(); // Stop the clock timer
            effortLogger.endClock(); // Invoke method to handle end of clock in EffortLogger
        });

        stopActivityPanel.add(stopInstructionLabel, BorderLayout.NORTH); // Add instruction label
        stopActivityPanel.add(stopButton, BorderLayout.CENTER); // Add stop button
        parentPanel.add(stopActivityPanel); // Add panel to the parent panel
    }

    /**
     * Helper method to display the tutorial.
     * This method is triggered by the Help button.
     *
     * @param parentPanel The parent panel where the tutorial might be displayed.
     * @param state The state of the tutorial to be displayed.
     */
    private static void turtorial_login(JPanel parentPanel, int state) {
        // Create an instance of EffortLoggerHelp
        EffortLoggerHelp effortLoggerHelpInstance = new EffortLoggerHelp();

        // Show the EffortLoggerHelp instance
        effortLoggerHelpInstance.setVisible(true);
    }
}