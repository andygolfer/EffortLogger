package EffortLogger;

//
//File Name: EffortLogger.java
//Creator: Dylan O'Hara
//Last Updated: 11/28/2023
//Project: CSE360 Team Th15 EFFORTLOGGER V2
//Description: This code represents an effort logging system, where each effort's start and end times are captured, 
//             along with other details like life cycle phase and category. It formats these details and stores them 
//             in an encrypted file. The class also includes methods for handling test user data and setting or retrieving 
//             user file paths.

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class EffortLogger 
{
    // Variables to track various aspects of logged efforts
    int counter = 0; // Counter for the number of logged efforts
    long startTime; // Start time of an effort in milliseconds
    long endTime; // End time of an effort in milliseconds
    long deltaTime; // Duration of the effort in milliseconds
    String formattedStartTime; // Start time formatted as HH:mm:ss
    String formattedEndTime; // End time formatted as HH:mm:ss
    String formattedDeltaTime; // Duration formatted as HH:mm:ss
    String lifeCycle; // Life cycle phase associated with the effort
    String effortCategory; // Category of the effort
    String formattedDate; // Current date formatted as MM/dd/yyyy
    private static Path userFile; // Path to the file where user data is stored

    /**
     * Starts the clock for tracking effort, recording the start time and initializing related data.
     *
     * @param selectedLifeCycle The life cycle phase associated with the effort.
     * @param selectedEffortCategory The category of the effort.
     */
    public void startClock(String selectedLifeCycle, String selectedEffortCategory) 
    {
        startTime = System.currentTimeMillis(); // Capture current time as start time
        Date currentDate = new Date(startTime); // Convert milliseconds to Date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        formattedStartTime = sdf.format(currentDate); // Format start time
        lifeCycle = selectedLifeCycle; // Set life cycle phase
        effortCategory = selectedEffortCategory; // Set effort category
        formattedDate = getDate(); // Get current date in formatted form
        counter++; // Increment effort count
    }

    /**
     * Ends the clock for tracking effort, calculating the duration and logging the effort.
     */
    public void endClock() 
    {
        endTime = System.currentTimeMillis(); // Capture current time as end time
        Date currentDate = new Date(endTime); // Convert milliseconds to Date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        formattedEndTime = sdf.format(currentDate); // Format end time

        // Calculate duration of the effort
        deltaTime = endTime - startTime;
        long seconds = deltaTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        formattedDeltaTime = String.format("%02d:%02d:%02d", hours, minutes, seconds); // Format duration

        produceLog(); // Log the effort details
    }

    /**
     * Retrieves the current date formatted as MM/dd/yyyy.
     *
     * @return Formatted date string.
     */
    private String getDate() 
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        return sdf.format(currentDate); // Format and return the date
    }

    /**
     * Logs the effort details into a file and encrypts the file.
     */
    public void produceLog() 
    {
        String formattedCounter = Integer.toString(counter); // Convert counter to a string

        // Encrypt and prepare data for storage
        String encryptedData = EncryptDecrypt.encryptString(formattedCounter + formattedDate + formattedStartTime + formattedEndTime + formattedDeltaTime + lifeCycle + effortCategory + "\n");

        try 
        {
            // Check if user file exists; if not, create a temporary file for the TestUser
            if (userFile == null) 
            {
                File tempFile = createTempUserFile("TestUser");

                // Encrypt the file and store it in the "user_info_encrypted" directory
                EncryptDecrypt.encryptFile(tempFile.getAbsolutePath(), "user_info_encrypted", true);

                // Construct the path for the encrypted file
                String encryptedFileName = "TestUser_encrypted.txt";
                Path encryptedFilePath = Paths.get(System.getProperty("user.dir"), "user_info_encrypted", encryptedFileName);
                userFile = encryptedFilePath;

                tempFile.delete(); // Delete the temporary file
            }

            // Store the encrypted data in the user file
            EncryptDecrypt.storeEncryptedFile(encryptedData, userFile, "user_info_encrypted", false);
        }

        catch (IOException e) 
        {
            e.printStackTrace(); // Handle any IO exceptions
        }
    }

    /**
     * Creates a temporary file for a test user with predefined content.
     *
     * @param username The username for the test user.
     * @return The created temporary file.
     * @throws IOException If an I/O error occurs.
     */
    public static File createTempUserFile(String username) throws IOException 
    {
        String fileName = username + ".txt"; // Construct file name
        File tempFile = new File(System.getProperty("user.dir"), fileName);

        // Define the content for the test user
        String content = "Name: test user\nUsername: user\nPassword: password\n";
        Files.writeString(tempFile.toPath(), content); // Write content to the file

        return tempFile; // Return the created file
    }

    /**
     * Sets the user information file path.
     *
     * @param userFilePath The path to the user information file.
     */
    public static void setUserInfo(Path userFilePath) 
    {
        userFile = userFilePath; // Set the user file path
    }

    /**
     * Retrieves the user information file path.
     *
     * @return Path to the user information file.
     */
    public static Path getUserFile() 
    {
        return userFile; // Return the user file path
    }
}