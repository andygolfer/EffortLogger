package EffortLogger;

//
//File Name: PlanningPoker.java
//Creator: Jana Steinborn
//Last Updated: 11/28/2023
//Project: CSE360 Team Th15 EFFORTLOGGER V2
//Description: This class represents a Planning Poker window in the Effort Logger application. It is responsible for setting up and managing the planning poker session.
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * 
 */
@SuppressWarnings("serial")
public class PlanningPoker extends JFrame {

    @SuppressWarnings("unused")
	private JPanel planningPokerPanel;
    private int userCount;
    private String backlogItemName;
    private String projectName;
    private String programmingLanguage;
    private String keywords;
    private JTextArea backlogItemInfoArea;
    private ArrayList<Integer> cardSelections = new ArrayList<>();
    private int currentUserNumber = 1;
    private JTextField userNumberField;
    private JTextArea finalInfoTextArea;

    /**
     * Constructor of the PlanningPoker class.
     * It initializes the user interface of the planning poker window.
     */
    public PlanningPoker() {
        initializeUI();
    }

    /**
     * Initializes the user interface of the planning poker window.
     * It sets up the layout and components for the planning poker session.
     */
    private void initializeUI() {
        // Create the welcome panel with instructions and a button to start the planning poker
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h1>Welcome to Planning Poker</h1>" +
                "<p>Click below when you're ready to start.</p></div></html>", SwingConstants.CENTER);
        JButton clickToStartButton = new JButton("Click to Start Planning Poker");

        // Add action listener to the start button to prompt for user count and project details
        clickToStartButton.addActionListener(e -> {
            try {
                promptForUserCountAndDetails();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // Set up the content panel with the welcome label and start button
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.add(clickToStartButton, BorderLayout.SOUTH);

        // Configure the frame settings
        this.setContentPane(contentPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 600);
        this.setLocationRelativeTo(null); // Center the frame
    }

    /**
     * Prompts the user for the number of participants and project details.
     * It collects information like backlog item name, project name, programming language, and keywords.
     *
     * @throws IOException If an I/O error occurs
     */
    private void promptForUserCountAndDetails() throws IOException {
        int userCount = promptForUserCount();
        if (userCount > 0) {
            // Setup a panel to collect project details from the user
            JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

            JTextField backlogItemNameField = new JTextField();
            JTextField projectNameField = new JTextField();
            JTextField programmingLanguageField = new JTextField();
            JTextField keywordsField = new JTextField();

            // Add labels and text fields to the panel
            detailsPanel.add(new JLabel("Backlog Item Name:"));
            detailsPanel.add(backlogItemNameField);
            detailsPanel.add(new JLabel("Project Name:"));
            detailsPanel.add(projectNameField);
            detailsPanel.add(new JLabel("Programming Language:"));
            detailsPanel.add(programmingLanguageField);
            detailsPanel.add(new JLabel("Keywords (optional):"));
            detailsPanel.add(keywordsField);

            // Show a dialog to collect project details
            int result = JOptionPane.showConfirmDialog(this, detailsPanel, 
                    "Enter Project Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // Process the information entered by the user
            if (result == JOptionPane.OK_OPTION) {
                String backlogItemName = backlogItemNameField.getText();
                String projectName = projectNameField.getText();
                String programmingLanguage = programmingLanguageField.getText();
                String keywords = keywordsField.getText();

                // Ensure required fields are filled
                if (!backlogItemName.isEmpty() && !projectName.isEmpty() && !programmingLanguage.isEmpty()) {
                    initiateNewBacklogItem(userCount, backlogItemName, projectName, programmingLanguage, keywords);
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields.",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Prompts the user to enter the number of users participating in the planning poker.
     *
     * @return The number of users as an integer, or 0 if an invalid number is entered.
     */
    private int promptForUserCount() {
        String userCountStr = JOptionPane.showInputDialog(this, "Enter the number of planning poker users:");
        try {
            // Attempt to parse the entered string into an integer
            return Integer.parseInt(userCountStr);
        } catch (NumberFormatException e) {
            // Show an error message if the input is not a valid number
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return 0; // Return 0 to signify invalid input
        }
    }

    /**
     * Initiates a new backlog item for the planning poker session with the given details.
     *
     * @param userCount The number of users in the planning poker session.
     * @param backlogItemName Name of the backlog item.
     * @param projectName Name of the project.
     * @param programmingLanguage Programming language used in the project.
     * @param keywords Keywords associated with the project or backlog item.
     * @throws IOException If an I/O error occurs.
     */
    private void initiateNewBacklogItem(int userCount, String backlogItemName, String projectName, String programmingLanguage, String keywords) throws IOException {
        // Set the details for the new backlog item
        this.userCount = userCount;
        this.backlogItemName = backlogItemName;
        this.projectName = projectName;
        this.programmingLanguage = programmingLanguage;
        this.keywords = keywords;
        updateBacklogItemInfoArea();

        this.dispose(); // Close the current window

        // Open the window for the planning poker cards session
        openPlanningPokerCardsWindow();
    }

    /**
     * Prompts the user to choose the next action after a planning poker session.
     * The user can choose to search historical data or estimate effort without historical data.
     */
    private void promptForNextAction() {
        String[] options = {"Search Historical Data", "Estimate Effort Without Historical Data"};
        int response = JOptionPane.showOptionDialog(this, "Would you like to search for historical data or estimate effort without it?",
                "Choose an Option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        this.dispose(); // Close the current window

        // Handle the user's response
        if (response == 0) {
            performSearch(true);
        } else if (response == 1) {
            this.dispose(); // Close the window if no further action is needed
        }
    }

    /**
     * Performs a search for historical data based on a user-provided search term.
     * Optionally, it can open the planning poker cards window after the search.
     *
     * @param openPokerCardsWindow Whether to open the planning poker cards window after searching.
     */
    private void performSearch(boolean openPokerCardsWindow) {
        String searchTerm = JOptionPane.showInputDialog(null, "Enter search term for historical data:");
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            try {
                File[] matchingFiles = EncryptDecrypt.searchPlanningPokerFiles(searchTerm.trim());
                if (matchingFiles.length > 0) {
                    displaySearchResults(matchingFiles);
                    if (openPokerCardsWindow) {
                        openPlanningPokerCardsWindow();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No matching historical data found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                    new PlanningPoker().promptForNextAction(); // Reopen the main window for further actions
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "An error occurred while searching.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            new PlanningPoker().promptForNextAction(); // Reopen the main window for further actions
        }
    }

    /**
     * Displays the search results in a dialog.
     * Allows the user to select a file from the search results and view its content.
     *
     * @param results An array of File objects representing the search results.
     */
    private void displaySearchResults(File[] results) {
        JDialog resultsDialog = new JDialog(this, "Search Results", Dialog.ModalityType.MODELESS);
        resultsDialog.setLayout(new BorderLayout());

        // Convert file names to a vector for display in a combo box
        Vector<String> fileNames = new Vector<>();
        for (File file : results) {
            fileNames.add(file.getName());
        }
        JComboBox<String> fileComboBox = new JComboBox<>(fileNames);

        JTextArea fileContentArea = new JTextArea();
        fileContentArea.setEditable(false);
        fileContentArea.setLineWrap(true);
        fileContentArea.setWrapStyleWord(true);

        // Add an action listener to update the content area when a file is selected
        fileComboBox.addActionListener(e -> {
            String selectedFileName = (String) fileComboBox.getSelectedItem();
            File selectedFile = Arrays.stream(results).filter(file -> file.getName().equals(selectedFileName)).findFirst().orElse(null);
            if (selectedFile != null) {
                try {
                    String content = Files.readString(selectedFile.toPath());
                    fileContentArea.setText(content);
                    fileContentArea.setCaretPosition(0); // Reset scroll position to the top
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(resultsDialog, "Error reading file content.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JScrollPane contentScrollPane = new JScrollPane(fileContentArea);
        contentScrollPane.setPreferredSize(new Dimension(350, 200));

        // Add components to the dialog
        resultsDialog.add(fileComboBox, BorderLayout.NORTH);
        resultsDialog.add(contentScrollPane, BorderLayout.CENTER);

        // Add a window listener to clean up temporary files on closing
        resultsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (File file : results) {
                    boolean isDeleted = file.delete();
                    if (!isDeleted) {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        });

        resultsDialog.setSize(400, 300);
        resultsDialog.setLocationRelativeTo(this);
        resultsDialog.setVisible(true);
    }

    /**
     * Opens a new window for the planning poker cards.
     * Allows users to select card values and aggregates these selections for estimating effort.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void openPlanningPokerCardsWindow() throws IOException {
        JFrame pokerCardsFrame = new JFrame("Planning Poker");
        pokerCardsFrame.setLayout(new BorderLayout());

        JLabel cardsLabel = new JLabel("Planning Poker Cards");
        cardsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel cardsPanel = new JPanel(new FlowLayout());
        String[] pokerCardValues = {"0", "1", "2", "3", "5", "8", "13", "20"};

        // Create buttons for each poker card value and add action listeners
        for (String cardValue : pokerCardValues) {
            JButton cardButton = new JButton(cardValue);
            cardButton.addActionListener(e -> {
                int cardVal = Integer.parseInt(cardValue);
                cardSelections.add(cardVal);
                if (currentUserNumber < userCount) {
                    currentUserNumber++;
                    userNumberField.setText("User " + currentUserNumber + " of " + userCount);
                } else {
                    // Calculate average of selected values and display results
                    double average = cardSelections.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    try {
                        showAverageAndBacklogInfo(average);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    pokerCardsFrame.dispose(); // Close the poker cards window
                }
            });
            cardsPanel.add(cardButton);
        }

        JButton searchButton = new JButton("Search Historical Data");
        searchButton.addActionListener(e -> performSearch(false));
        cardsPanel.add(searchButton);

        JLabel currentBacklogItemLabel = new JLabel("Current Backlog Item");
        currentBacklogItemLabel.setHorizontalAlignment(SwingConstants.CENTER);

        backlogItemInfoArea = new JTextArea();
        updateBacklogItemInfoArea();
        backlogItemInfoArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(backlogItemInfoArea);

        JButton editInfoButton = new JButton("Edit Backlog Item Info");
        editInfoButton.addActionListener(e -> editBacklogItemInfo());

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(currentBacklogItemLabel, BorderLayout.NORTH);
        infoPanel.add(infoScrollPane, BorderLayout.CENTER);
        infoPanel.add(editInfoButton, BorderLayout.SOUTH);

        userNumberField = new JTextField("User 1 of " + userCount, 10);
        userNumberField.setEditable(false);

        // Add components to the poker cards frame
        pokerCardsFrame.add(cardsLabel, BorderLayout.NORTH);
        pokerCardsFrame.add(cardsPanel, BorderLayout.CENTER);
        pokerCardsFrame.add(userNumberField, BorderLayout.EAST);
        pokerCardsFrame.add(infoPanel, BorderLayout.SOUTH);

        pokerCardsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pokerCardsFrame.setSize(600, 400);
        pokerCardsFrame.setLocationRelativeTo(null);
        pokerCardsFrame.setVisible(true);
    }

    /**
     * Allows editing of the backlog item information.
     * A dialog is presented for the user to modify details like backlog item name, project name, etc.
     */
    private void editBacklogItemInfo() {
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        // Initialize text fields with current backlog item information
        JTextField backlogItemNameField = new JTextField(this.backlogItemName);
        JTextField projectNameField = new JTextField(this.projectName);
        JTextField programmingLanguageField = new JTextField(this.programmingLanguage);
        JTextField keywordsField = new JTextField(this.keywords);

        // Add labels and text fields to the panel
        detailsPanel.add(new JLabel("Backlog Item Name:"));
        detailsPanel.add(backlogItemNameField);
        detailsPanel.add(new JLabel("Project Name:"));
        detailsPanel.add(projectNameField);
        detailsPanel.add(new JLabel("Programming Language:"));
        detailsPanel.add(programmingLanguageField);
        detailsPanel.add(new JLabel("Keywords (optional):"));
        detailsPanel.add(keywordsField);

        // Show a confirmation dialog to get user input
        int result = JOptionPane.showConfirmDialog(this, detailsPanel,
                "Edit Project Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Update the backlog item information with the user's input
            this.backlogItemName = backlogItemNameField.getText();
            this.projectName = projectNameField.getText();
            this.programmingLanguage = programmingLanguageField.getText();
            this.keywords = keywordsField.getText();

            updateBacklogItemInfoArea(); // Refresh the displayed information

            // Update the final information text area if it exists
            if (finalInfoTextArea != null) {
                String updatedInfoText = String.format("Backlog Item: %s\nProject: %s\nLanguage: %s\nKeywords: %s\nUsers: %d\nAverage Estimate: %.2f",
                        backlogItemName, projectName, programmingLanguage, keywords, userCount, calculateAverageEstimate());
                finalInfoTextArea.setText(updatedInfoText);
            }
        }
    }

    /**
     * Updates the text area that displays the backlog item information.
     */
    private void updateBacklogItemInfoArea() {
        if (backlogItemInfoArea != null) {
            String info = String.format("Backlog Item: %s\nProject: %s\nLanguage: %s\nKeywords: %s\nUsers: %d",
                    backlogItemName, projectName, programmingLanguage, keywords, userCount);
            backlogItemInfoArea.setText(info);
        }
    }

    /**
     * Shows the average estimate and backlog information in a new window.
     * Provides options to edit backlog item info and finalize the backlog item.
     *
     * @param average The calculated average estimate.
     * @throws IOException If an I/O error occurs.
     */
    private void showAverageAndBacklogInfo(double average) throws IOException {
        JFrame infoFrame = new JFrame("Backlog Item Information");
        infoFrame.setLayout(new BorderLayout());

        JTextArea infoTextArea = new JTextArea();
        String infoText = String.format("Backlog Item: %s\nProject: %s\nLanguage: %s\nKeywords: %s\nUsers: %d\nAverage Estimate: %.2f",
                backlogItemName, projectName, programmingLanguage, keywords, userCount, average);
        infoTextArea.setText(infoText);
        infoTextArea.setEditable(false);

        finalInfoTextArea = infoTextArea; // Store reference for future updates

        JScrollPane infoScrollPane = new JScrollPane(infoTextArea);
        infoScrollPane.setPreferredSize(new Dimension(400, 300));

        // Buttons for editing info and finalizing the backlog item
        JButton editInfoButton = new JButton("Edit Backlog Item Info");
        editInfoButton.addActionListener(e -> editBacklogItemInfo());
        JButton finalizeButton = new JButton("Finalize Backlog Item");
        finalizeButton.addActionListener(e -> {
            try {
                File tempFile = createTempBacklogFile();
                EncryptDecrypt.encryptFile(tempFile.getAbsolutePath(), "planning_poker_data_encrypted", true);
                tempFile.delete();
                infoFrame.dispose(); // Close the info frame
                EffortLoggerConsole.frame.setState(Frame.NORMAL);
                EffortLoggerConsole.frame.toFront(); // Bring the main frame to the front
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(infoFrame, "Error during file encryption.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(editInfoButton, BorderLayout.NORTH);
        bottomPanel.add(finalizeButton, BorderLayout.SOUTH);

        // Add components to the frame
        infoFrame.add(infoScrollPane, BorderLayout.CENTER);
        infoFrame.add(bottomPanel, BorderLayout.SOUTH);

        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(500, 400);
        infoFrame.setLocationRelativeTo(null);
        infoFrame.setVisible(true);
    }

    /**
     * Calculates the average of the selected estimates in the planning poker session.
     *
     * @return The calculated average as a double.
     */
    private double calculateAverageEstimate() {
        return cardSelections.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    /**
     * Creates a temporary file for storing backlog item information.
     * The file name is generated based on the backlog item name and the content is formatted.
     *
     * @return The created temporary file.
     * @throws IOException If an I/O error occurs.
     */
    private File createTempBacklogFile() throws IOException {
        String fileName = backlogItemName.replaceAll("\\s+", "_") + ".txt";
        File tempFile = new File(System.getProperty("user.dir"), fileName);

        String content = String.format("Backlog Item: %s\nProject: %s\nLanguage: %s\nKeywords: %s\nUsers: %d\nAverage Estimate: %.2f",
                backlogItemName, projectName, programmingLanguage, keywords, userCount, calculateAverageEstimate());
        
        Files.writeString(tempFile.toPath(), content); // Write the backlog information to the file
        return tempFile;
    }
}

