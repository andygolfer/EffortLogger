//
// File Name: EncryptDecrypt
// Creator: Zack Beckwith
// Date: 10/25/2023
// Project: CSE360 Team Th15
// Description: This file contains the encryption/decryption algorithm for team Th15 EffortLogger project for CSE360 Fall 2023. It uses a Caesarian
// 		Encryption method which shifts each character in the ASCII table by a specified key (hard coded here, can be added as a parameter
//		to further enhance security. If first searches for the file, parses the file putting the information into a character array. It then
//		uses the Caesarian Encryption method to either encrypt or decrypt. It then stores the file with a "_encrypted" addition to the end of
//		the file name to depict if the file is encrypted. It then deletes the original file after completing either the encryption or decryption
//		processes. Each method in this file has been defined as static which allows for the methods to be called without creating an object. 
//

package caesarianEncryption;

import java.io.IOException;
import java.nio.file.*;

public class EncryptDecrypt {

// --------------------------------------------encryption methods--------------------------------------------------------------
    
    public static void encryptFile() throws IOException {
        Path currentWorkingDirectory = Paths.get(System.getProperty("user.dir"));  // Get the current working directory
        Path filePath = currentWorkingDirectory.resolve("output.txt");  // Construct the full file path

        int key = 10;  // Key for the Caesar cipher, can be passed in as a parameter.

        if (!Files.exists(filePath)) {
            System.out.print("Error: File not found");
            return;
        }

        String unencryptedFileContent = Files.readString(filePath);  // Read the content of "output.txt" in the current working directory as a string
        encryptionAlgorithm(unencryptedFileContent, key, filePath);  // Encrypt the content and store the result in a new file
    }

    
    private static void encryptionAlgorithm(String fileContent, int key, Path originalFilePath) throws IOException	// Method that performs the actual Caesar cipher encryption
    {	 
   
        char[] unencryptedFileContent = fileContent.toCharArray();	// Convert the file content to an array of characters
        StringBuilder encryptedFile = new StringBuilder();	// StringBuilder to store the encrypted content
        
        for (int i = 0; i < unencryptedFileContent.length; i++)	// Loop through each character in the array
        {
            char encryptedChar = (char) (unencryptedFileContent[i] + key);
            encryptedFile.append(encryptedChar);	// Append the shifted character to the encrypted content
        }
        
        String encryptedContent = encryptedFile.toString();	// Convert the StringBuilder to a string
        encryptedFile = null;	// Clear the StringBuilder to free up memory
        storeEncryptedFile(encryptedContent, originalFilePath);	// Store the encrypted content in a new file and delete the original file
    }
    
    
    private static void storeEncryptedFile(String encryptedContent, Path originalFilePath) throws IOException	// Method to store the encrypted content in a new file
    {
        String originalFileName = originalFilePath.getFileName().toString();	// Get the file name from the original file path
        String encryptedFileName = originalFileName.replaceFirst("[.][^.]+$", "") + "_encrypted.txt";	// Create the new file name by appending "_encrypted.txt" to the original file name (excluding the file extension)
        String encryptedFilePath = originalFilePath.getParent().resolve(encryptedFileName).toString();	// Create the path for the new encrypted file
        Files.writeString(Path.of(encryptedFilePath), encryptedContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);	// Write the encrypted content to the new file, create the file if it does not exist, and overwrite if it does. May want to turn off the "Truncate_Existing" to keep the program from overriting an existing file.
        Files.delete(originalFilePath);  // Delete the original file. THIS CAN BE TURNED OFF TO KEEP THE ORIGINAL FILE (Be careful when decrypting though as the decrypt will store the decrypted file as the original file name)
    }
    
// --------------------------------------------decryption methods--------------------------------------------------------------
    
    public static void decryptFile(String inputFilePath) throws IOException // Method to decrypt a file given its path
    { 
        Path filePath = Paths.get(inputFilePath);	// Convert the string file path to a Path object
        int key = 10;	// Key for the Caesar cipher
        
        if (!Files.exists(filePath)) {	// Check if the file exists, print an error and return if it does not
            System.out.print("Error: File not found");
            return;
        }
        
        String encryptedFileContent = Files.readString(filePath);	// Read the content of the file as a string
        decryptionAlgorithm(encryptedFileContent, key, filePath);	// Decrypt the content and store the result in a new file
    }

    private static void decryptionAlgorithm(String fileContent, int key, Path originalFilePath) throws IOException	// Method that performs the actual Caesar cipher decryption
    {
        char[] decryptedFileContent = fileContent.toCharArray();	// Convert the file content to an array of characters
        StringBuilder decryptedFile = new StringBuilder();	// StringBuilder to store the decrypted content
        
        for (int i = 0; i < decryptedFileContent.length; i++)	 // Loop through each character in the array
        {
            char decryptedChar = (char) (decryptedFileContent[i] - key);	// Shift the character back by the key value to decrypt it
            decryptedFile.append(decryptedChar);	// Append the decrypted character to the decrypted content
        }
        
        String decryptedContent = decryptedFile.toString();	// Convert the StringBuilder to a string
        decryptedFile = null; // Clear the StringBuilder to free up memory
        storeDecryptedFile(decryptedContent, originalFilePath); // Store the decrypted content in a new file and delete the original file
    }

    private static void storeDecryptedFile(String decryptedContent, Path originalFilePath) throws IOException	 // Method to store the decrypted content in a new file
    {
        
        String originalFileName = originalFilePath.getFileName().toString();	// Get the file name from the original file path
        String decryptedFileName = originalFileName.replaceFirst("[_][^_]+$", "") + ".txt";	// Create the new file name by removing "_encrypted" and appending ".txt"
        String decryptedFilePath = originalFilePath.getParent().resolve(decryptedFileName).toString();	// Create the path for the new decrypted file
        Files.writeString(Path.of(decryptedFilePath), decryptedContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);	// Write the decrypted content to the new file, create the file if it does not exist, and overwrite if it does. May want to turn off the "Truncate_Existing" to keep the program from overriting an existing file.
        Files.delete(originalFilePath);	// Delete the original file. THIS CAN BE TURNED OFF TO KEEP THE ORIGINAL FILE
    }
}