package EffortLogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.nio.file.*;

/**
 * This class represents an Effort Logger that logs time and effort data into a file.
 */
public class EffortLogger{
    int counter = 0; // Counter to keep track of the number of logged efforts
    long startTime; // Time when an effort is started
    long endTime; // Time when an effort is ended
    long deltaTime; // Duration of the effort in milliseconds
    String formattedStartTime; // Formatted start time in HH:mm:ss format
    String formattedEndTime; // Formatted end time in HH:mm:ss format
    String formattedDeltaTime; // Formatted duration in HH:mm:ss format
    String lifeCycle; // The life cycle associated with the effort
    String effortCategory; // The category of the effort
    String formattedDate; // Formatted date in MM/dd/yyyy format
    private static Path userFile;

    /**
     * Starts the effort timer and initializes relevant data.
     *
     * @param selectedLifeCycle The life cycle associated with the effort.
     * @param selectedEffortCategory The category of the effort.
     */
    public void startClock(String selectedLifeCycle, String selectedEffortCategory) {
        startTime = System.currentTimeMillis(); // Record the start time in milliseconds
        Date currentDate = new Date(startTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        formattedStartTime = sdf.format(currentDate); // Format the start time as HH:mm:ss
        lifeCycle = selectedLifeCycle;
        effortCategory = selectedEffortCategory;
        formattedDate = getDate(); // Get the formatted date
        counter++; // Increment the effort counter
    }

    /**
     * Ends the effort timer, calculates the duration, and logs the effort.
     */
    public void endClock() {
        endTime = System.currentTimeMillis(); // Record the end time in milliseconds
        Date currentDate = new Date(endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        formattedEndTime = sdf.format(currentDate); // Format the end time as HH:mm:ss

        deltaTime = endTime - startTime; // Calculate the duration in milliseconds
        long seconds = deltaTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;
        formattedDeltaTime = String.format("%02d:%02d:%02d", hours, minutes, seconds); // Format duration as HH:mm:ss

        produceLog(); // Log the effort
    }

    /**
     * Gets the current date in MM/dd/yyyy format.
     *
     * @return Formatted date.
     */
    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        return sdf.format(currentDate); // Format the current date as MM/dd/yyyy
    }

    /**
     * Logs the effort data into a file and encrypts the file.
     */
    public void produceLog() {
        String formattedCounter = Integer.toString(counter); // Convert counter to a string
        
        String encryptedData = EncryptDecrypt.encryptString(formattedCounter + formattedDate + formattedStartTime + formattedEndTime + formattedDeltaTime + lifeCycle + effortCategory + "\n");

        // Try to write data to a file in append mode (true)
        try {
        	EncryptDecrypt.storeEncryptedFile(encryptedData, userFile, "user_info_encrypted", false);
        } catch (IOException e) {
            e.printStackTrace(); // Handle any IO exceptions
        }

    }
    
    public static void setUserInfo(Path userFilePath)
    {
    	userFile = userFilePath;
    }
}
