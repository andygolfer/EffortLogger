package EffortLogger;

//File Name: EffortLoggerHelp.java
//Creator: Quy Hoang Nguyen
//Last Updated: 11/29/2023
//Project: CSE360 Team Th15 EFFORTLOGGER V2
//Description: 
//

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("serial")
public class EffortLoggerHelp extends JFrame {

    public EffortLoggerHelp() {
        // Configure the JFrame
        setTitle("Effort Logger Help");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create components
        JLabel infoLabel = new JLabel("<html>Grader Login:<br/>Username: user<br/>Password: password</html>");
        JButton tutorialButton = new JButton("Tutorial Videos");

        // Set the action for the tutorial button
        tutorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the web browser and navigate to the specified website
                openWebPage("https://drive.google.com/drive/folders/13hQGDQ12JJJc_3w4qyRORS-VJb4d0nsi?usp=sharing");
            }
        });

        // Layout the components
        setLayout(new BorderLayout());
        add(infoLabel, BorderLayout.CENTER);
        add(tutorialButton, BorderLayout.SOUTH);
    }

    public void openWebPage(String url) {
        // Try to open the URL in the default web browser
        try {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open the web page.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}