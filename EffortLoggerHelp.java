package EffortLogger;
//File Name: EffortLoggerHelp.java
//Creator: Quy Hoang Nguyen
//Last Updated: 11/29/2023
//Project: CSE360 Team Th15 EFFORTLOGGER V2
//Description: This class is responsible for the creation of the Tutorial program. The class will
//be activated when the user clicks the help button. After that, the program will lead the user to a 
//google drive file that contains all the necessary videos to familiarize themselves with the whole program. 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.File;
import java.io.FileWriter;

public class EffortLoggerHelp extends JFrame {
    // constant for feed back
    private static final String FOLDER_PATH = "user_feedback_folder";
    private static final String FILE_NAME = "user_feedback.txt";
    // Constructor
    public EffortLoggerHelp() {
        // Configure the JFrame
        setTitle("Effort Logger Help");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create components
        JLabel infoLabel = new JLabel("<html>Grader Login:<br/>Username: user<br/>Password: password</html>");
        JButton tutorialButton = new JButton("Tutorial Videos");
        JButton feedbackButton = new JButton("Feedback");
        JButton returnButton = new JButton("Return");
        // Set the action for the tutorial button
        tutorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the web browser and navigate to the specified website
                openWebPage("https://drive.google.com/drive/folders/13hQGDQ12JJJc_3w4qyRORS-VJb4d0nsi?usp=sharing");
            }
        });

        feedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle feedback button click
                Feedback();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle feedback button click
                dispose();
            }
        });

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(feedbackButton, BorderLayout.NORTH);
        buttonPanel.add(tutorialButton, BorderLayout.SOUTH);

        // Layout the components in the main frame
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(infoLabel, BorderLayout.CENTER);
        add(returnButton, BorderLayout.SOUTH);
    }

    // Method to open a web page
    public void openWebPage(String url) {
        // Try to open the URL in the default web browser
        try {
            // Check if Desktop API is supported
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    
            // Check if BROWSE action is supported
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                // Use the Desktop API to open the given URL
                desktop.browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            // Handle exceptions,
            e.printStackTrace();
            
            // Display an error message using JOptionPane if opening the web page fails
            JOptionPane.showMessageDialog(this, "Failed to open the web page.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void Feedback() {
        // Create the main frame
        JFrame frame = new JFrame("Feedback");

            frame.setSize(300, 200);
    
            // Create a JTextArea
            JTextArea textArea = new JTextArea(50, 50); 
    
            // Create a JButton to trigger an action
            JButton button = new JButton("submit");
            JButton returnbutton = new JButton("Return");
    
            // Create a JScrollPane and add the JTextArea to it
            JScrollPane scrollPane = new JScrollPane(textArea);

            // Create a JLabel to display the result
            JLabel resultLabel = new JLabel("");
    
            // Set layout manager to the frame
            frame.setLayout(new java.awt.FlowLayout());
        

            // Add components to the frame
            frame.add(scrollPane);
            frame.add(button);
            frame.add(resultLabel);
            frame.add(returnbutton);


        // Add action listener to the button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                System.out.println("Button clicked!");
             String newText = textArea.getText();

                // Create a folder if it doesn't exist
                File folder = new File(FOLDER_PATH);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // Create or append text to the file
                try (FileWriter writer = new FileWriter(new File(folder, FILE_NAME), true)) {
                    // Append a newline if the file already contains text
                    if (folder.listFiles().length > 0) {
                        writer.write("\n");
                    }
                    // Write the new text
                    writer.write(newText);
                    resultLabel.setText("Text saved to file.");
                    frame.pack(); // make the program expand for the above text
                } catch (IOException ex) {
                    ex.printStackTrace();
                    resultLabel.setText("Error saving text to file.");
                    frame.pack();
                }
            }
            });
            
            returnbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This method is called when the button is clicked
                System.out.println("Button clicked!");

                // return
                frame.dispose();
            }
        });

    
        // Set the frame to pack its components and set visibility
        frame.pack();
        frame.setVisible(true);
    }
}