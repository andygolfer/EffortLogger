package EffortLogger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EffortLoggerHelp extends JFrame {

    public EffortLoggerHelp() {
        // Configure the JFrame
        setTitle("Effort Logger Help");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
    }

    public void GifExample(int state) {
        // Construct the file path for the GIF based on the state
        String fileName = state == 0 ? "state0.gif" : "state1.gif";
        Path gifPath = Paths.get(System.getProperty("user.dir")).resolve(fileName);

        // Try to load the GIF file and create the label
        JLabel gifLabel;
        try {
            BufferedImage gifImage = ImageIO.read(gifPath.toFile());
            ImageIcon gifIcon = new ImageIcon(gifImage);
            gifLabel = new JLabel(gifIcon);
        } catch (IOException e) {
            // If there's an exception, log it and create a label indicating failure
            e.printStackTrace();
            gifLabel = new JLabel("Failed to load GIF image.");
        }

        // Create a return button
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> dispose()); // Close the current window

        // Create a panel and add the label and button to it
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gifLabel, BorderLayout.CENTER);
        panel.add(returnButton, BorderLayout.SOUTH);

        // Add the panel to the frame
        setContentPane(panel);

        // Show the frame
        pack();
        setVisible(true);
    }
}
