package EffortLogger;

//
//File Name: EncryptDecrypt
//Creator: Zack Beckwith
//Date: 10/25/2023
//Project: CSE360 Team Th15
//Description: This file contains the encryption/decryption algorithm for team Th15 EffortLogger project for CSE360 Fall 2023. It uses a Caesarian
//		Encryption method which shifts each character in the ASCII table by a specified key (hard coded here, can be added as a parameter
//		to further enhance security. If first searches for the file, parses the file putting the information into a character array. It then
//		uses the Caesarian Encryption method to either encrypt or decrypt. It then stores the file with a "_encrypted" addition to the end of
//		the file name to depict if the file is encrypted. It then deletes the original file after completing either the encryption or decryption
//		processes. Each method in this file has been defined as static which allows for the methods to be called without creating an object. 
//

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class EncryptDecrypt {

    //--------------------------------------------encryption methods--------------------------------------------------------------

    /**
     * Encrypts the content of a specified file.
     *
     * @param fileName Name of the file to be encrypted.
     * @param fileType The type of the file being encrypted (used for directory naming).
     * @param newFolder Flag indicating whether to create a new folder for the encrypted file.
     * @throws IOException If an I/O error occurs.
     */
    public static void encryptFile(String fileName, String fileType, boolean newFolder) throws IOException {
        // Get the current working directory
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
       
        // Construct the full file path
        Path originalFilePath = currentWorkingDirectory.resolve(fileName);

        // Caesar cipher key
        int key = 10;

        // Check if the file exists
        if (!Files.exists(originalFilePath)) {
            System.out.print("Error: File not found");
            return;
        }

        // Read the content of the file
        String unencryptedFileContent = Files.readString(originalFilePath);
        
        // Encrypt the content and store the result in a new file
        encryptionAlgorithm(unencryptedFileContent, key, originalFilePath, fileType, newFolder);
    }

    /**
     * Performs the actual Caesar cipher encryption.
     *
     * @param fileContent      The content of the file to be encrypted.
     * @param key              The key for the Caesar cipher.
     * @param originalFilePath The path of the original file.
     * @param fileType         The type of the file being encrypted.
     * @param newFolder        Flag to determine if a new folder should be created.
     * @throws IOException If an I/O error occurs.
     */
    private static void encryptionAlgorithm(String fileContent, int key, Path originalFilePath, String fileType, boolean newFolder) throws IOException {
        // Convert the file content to a character array
        char[] unencryptedFileContent = fileContent.toCharArray();
        
        // StringBuilder to store the encrypted content
        StringBuilder encryptedFile = new StringBuilder();

        // Loop through each character in the array and apply Caesar cipher encryption
        for (int i = 0; i < unencryptedFileContent.length; i++) {
            char encryptedChar = (char) (unencryptedFileContent[i] + key);
            encryptedFile.append(encryptedChar);
        }

        // Convert the StringBuilder to a string
        String encryptedContent = encryptedFile.toString();
        
        // Store the encrypted content in a new file
        storeEncryptedFile(encryptedContent, originalFilePath, fileType, newFolder);
    }

    /**
     * Stores the encrypted content in a new file, in the appropriate directory.
     *
     * @param encryptedContent The encrypted content to be written to the file.
     * @param originalFilePath The path of the original file.
     * @param fileType         The type of the file being encrypted.
     * @param newFolder        Flag indicating whether to create a new folder.
     * @throws IOException If an I/O error occurs.
     */
    public static void storeEncryptedFile(String encryptedContent, Path originalFilePath, String fileType, boolean newFolder) throws IOException {
        Path folderPath;

        // Determine the folder path based on the newFolder flag
        if (newFolder) {
            folderPath = originalFilePath.getParent().resolve(fileType);
            Files.createDirectories(folderPath);
        } else {
            folderPath = originalFilePath.getParent();
        }

        // Construct the file name for the encrypted file
        String originalFileName = originalFilePath.getFileName().toString();
        if (!originalFilePath.toString().contains("_encrypted.txt")) {
            originalFileName = originalFileName.replaceFirst("[.][^.]+$", "") + "_encrypted.txt";
        }
        // Resolve the path for the new encrypted file
        Path encryptedFilePath = folderPath.resolve(originalFileName);

        // Write the encrypted content to the new file, appending if it exists
        Files.writeString(encryptedFilePath, encryptedContent, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    //--------------------------------------------decryption methods--------------------------------------------------------------

    /**
     * Decrypts a specified file.
     *
     * @param fileFolder The folder containing the file to be decrypted.
     * @param fileName   The name of the file to be decrypted.
     * @throws IOException If an I/O error occurs.
     */
    public static void decryptFile(String fileFolder, String fileName) throws IOException {
        // Get the current working directory
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        
        // Resolve the path to the file to be decrypted
        Path originalFilePath = currentWorkingDirectory.resolve(fileFolder).resolve(fileName);

        // Key for the Caesar cipher
        int key = 10;

        // Check if the file exists
        if (!Files.exists(originalFilePath)) {
            System.out.print("Error: File not found");
            return;
        }

        // Read the encrypted content of the file
        String encryptedFileContent = Files.readString(originalFilePath);
        
        // Decrypt the content and store the result in a new file
        decryptionAlgorithm(encryptedFileContent, key, originalFilePath);
    }

    /**
     * Performs the actual Caesar cipher decryption.
     *
     * @param fileContent      The content of the file to be decrypted.
     * @param key              The key for the Caesar cipher.
     * @param originalFilePath The path of the original file.
     * @throws IOException If an I/O error occurs.
     */
    private static void decryptionAlgorithm(String fileContent, int key, Path originalFilePath) throws IOException {
        // Convert the file content to a character array
        char[] decryptedFileContent = fileContent.toCharArray();
        
        // StringBuilder to store the decrypted content
        StringBuilder decryptedFile = new StringBuilder();

        // Loop through each character in the array and apply Caesar cipher decryption
        for (int i = 0; i < decryptedFileContent.length; i++) {
            char decryptedChar = (char) (decryptedFileContent[i] - key);
            decryptedFile.append(decryptedChar);
        }

        // Convert the StringBuilder to a string
        String decryptedContent = decryptedFile.toString();
        
        // Store the decrypted content in a new file
        storeDecryptedFile(decryptedContent, originalFilePath);
    }

    /**
     * Stores the decrypted content in a new file, replacing the original file name with a ".txt" extension.
     *
     * @param decryptedContent The decrypted content to be written to the file.
     * @param originalFilePath The path of the original encrypted file.
     * @throws IOException If an I/O error occurs.
     */
    private static void storeDecryptedFile(String decryptedContent, Path originalFilePath) throws IOException {
        // Construct the file name for the decrypted file
        String originalFileName = originalFilePath.getFileName().toString();
        String decryptedFileName = originalFileName.replaceFirst("[_][^_]+$", "") + ".txt";

        // Write the decrypted content to the new file, overwriting if it exists
        Files.writeString(Path.of(decryptedFileName), decryptedContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    //--------------------------------------------utility methods--------------------------------------------------------------

    /**
     * Searches for user files containing the specified username and password.
     *
     * @param userName The username to search for.
     * @param password The password to search for.
     * @return The path of the file if found, null otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public static Path searchUserFiles(String userName, String password) throws IOException {
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        File folder = new File(currentWorkingDirectory.toString() + "/user_info_encrypted");
        File[] listOfFiles = folder.listFiles();

        // Encrypt the username and password, including the newline character and the labels
        String encryptedUserName = encryptString("Username: " + userName + "\r");
        String encryptedPassword = encryptString("Password: " + password);

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String fileContent = Files.readString(file.toPath());
                if(fileContent.contains(encryptedUserName) && fileContent.contains(encryptedPassword)) 
                {
                    return file.toPath();
                }
            }
        }

        return null;
    }


    
    /**
     * Searches for Planning Poker files that contain the specified search term.
     *
     * @param searchTerm The term to search for within the Planning Poker files.
     * @return An array of Files that contain the encrypted search term.
     * @throws IOException If an I/O error occurs.
     */
    public static File[] searchPlanningPokerFiles(String searchTerm) throws IOException {
        // Determine the directory to search in
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        File folder = new File(currentWorkingDirectory.toString() + "/planning_poker_data_encrypted");
        File[] listOfFiles = folder.listFiles();

        // Check if the directory exists and contains files
        if (listOfFiles == null) {
            return new File[0];
        }

        List<File> workingFiles = new ArrayList<>();

        // Encrypt the search term for matching
        String encryptedSearchTerm = encryptString(searchTerm);

        // Search for files containing the encrypted search term
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String fileContent = Files.readString(file.toPath());
                if (fileContent.contains(encryptedSearchTerm)) {
                    workingFiles.add(file);
                }
            }
        }

        // Convert the list to an array and return
        return workingFiles.toArray(new File[0]);
    }
    
    
    /**
     * Encrypts a given string using the Caesar cipher.
     *
     * @param info The string to be encrypted.
     * @return The encrypted string.
     */
    public static String encryptString(String info) {
        // Convert the string to a character array
        char[] unencryptedString = info.toCharArray();
        // StringBuilder to store the encrypted string
        StringBuilder encryptedString = new StringBuilder();
        // Caesar cipher key
        int key = 10;

        // Encrypt each character and append to the StringBuilder
        for (int i = 0; i < unencryptedString.length; i++) {
            char encryptedChar = (char) (unencryptedString[i] + key);
            encryptedString.append(encryptedChar);
        }
        // Return the encrypted string
        return encryptedString.toString();
    }
}

