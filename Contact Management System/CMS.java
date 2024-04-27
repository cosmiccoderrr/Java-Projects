/* Project:- Contact Management  System
   Developer:- Kartik Verma
   Date:-27-04-2024 
   Time:- 12:06
   Day:- Saturday
*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CMS {
    public static void main(String[] args) {
        Greetings();
        Menu();
    }

    public static void Greetings() {
        System.out.println("\n");
        System.out.println("\t\t\t\t\t\tWelcome To The Contact Management System");
    }

    public static void Menu() {
        boolean exit = false; // Variable to control exiting the program
    
        try (Scanner sc = new Scanner(System.in)) {
            while (!exit) {
                System.out.println("1. Show Contacts");
                System.out.println("2. Add A Contact");
                System.out.println("3. Delete A Contact");
                System.out.println("4. Search A Contact");
                System.out.println("5. Exit"); // Option for exiting the program
    
                System.out.println("\nEnter Your Choice (1-5):");
    
                // Check if there is more input available
                if (sc.hasNextLine()) {
                    try {
                        int choice = Integer.parseInt(sc.nextLine()); // Read the choice as a line and parse to integer
                        switch (choice) {
                            case 1:
                                showContacts();
                                break;
                            case 2:
                                addContacts();
                                break;
                            case 3:
                                deleteContact();
                                break;
                            case 4:
                                searchContacts();
                                break;
                            case 5:
                                exit = true; // Set exit flag to true to exit the loop
                                System.out.println("Exiting the program...");
                                break;
                            default:
                                System.out.println("Invalid choice! Please enter a number from 1 to 5.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter a number from 1 to 5.");
                    }
                } else {
                    // Provide an option to exit the program when there's no input
                    exit = true;
                }
            }
        }
    }
    

    public static void showContacts() {
        List<String> contacts = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contacts.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading contacts.");
            e.printStackTrace();
            return;
        }
    
        // Sort the contacts alphabetically
        Collections.sort(contacts);
    
        // Display sorted contacts
        System.out.println("\nShowing Contacts Available\n");
        for (String contact : contacts) {
            System.out.println(contact);
        }
        System.out.println("\n");
    }
    

    public static void addContacts() {
        FileWriter writer = null;
        try (Scanner sc = new Scanner(System.in)) {
            writer = new FileWriter("contacts.csv", true);
            System.out.println("Enter the name:");
            String name = sc.nextLine().toUpperCase(); // Convert name to uppercase
            System.out.println("Enter the contact number:");
            String contact = sc.nextLine(); // Read the contact number
            writer.write(name + ":" + contact + "\n");
            // Flush the writer to ensure data is written immediately
            writer.flush();
            System.out.println("Contact added successfully!\n");
            // Consume the newline character
            sc.nextLine();
            // Return to the main menu
            Menu();
        } catch (IOException e) {
            System.out.println("An error occurred while adding the contact.\n");
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error occurred while closing the FileWriter.");
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    
    
    public static void deleteContact() {
        boolean contactDeleted = false; // Flag to indicate whether the contact was deleted
    
        try (Scanner sc = new Scanner(System.in);
             BufferedReader reader = new BufferedReader(new FileReader("contacts.csv"));
             FileWriter writer = new FileWriter("temp.csv")) {
            System.out.println("Enter the name of the contact you want to delete:");
            String nameToDelete = sc.next().toUpperCase(); // Convert input name to uppercase
    
            String line;
            boolean contactFound = false;
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String name = parts[0];
                if (!name.equals(nameToDelete)) {
                    writer.write(line + "\n");
                } else {
                    contactFound = true;
                }
            }
    
            if (contactFound) {
                System.out.println("Contact removed successfully!");
                contactDeleted = true; // Set the flag to true if contact was deleted
            } else {
                System.out.println("Contact not found!");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the contact.");
            e.printStackTrace();
        }
    
        // Replace the original file with the temporary file
        replaceFile("temp.csv", "contacts.csv");
        
        // If contact was deleted, inform the user and exit the program; otherwise, inform the user to try again
        if (contactDeleted) {
            System.out.println("\nExiting the program...");
            return; // Exit the method
        } else {
            System.out.println("\nPlease try again.");
        }
    }
    
    
    
    
    

    
    
    
    
    
    
    private static void replaceFile(String sourceFile, String targetFile) {
        try {
            java.nio.file.Files.move(java.nio.file.Paths.get(sourceFile), java.nio.file.Paths.get(targetFile),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("An error occurred while replacing the file.");
            e.printStackTrace();
        }
    }

    public static void searchContacts() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("How do you want to search the contact?");
            System.out.println("1. By Contact Name");
            System.out.println("2. By Contact Number");
            int choice = sc.nextInt();
    
            System.out.println("Enter the search term:");
            String searchTerm = sc.next().toUpperCase(); // Convert search term to uppercase
    
            List<String> contacts = new ArrayList<>();
    
            try (BufferedReader reader = new BufferedReader(new FileReader("contacts.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    contacts.add(line);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading contacts.");
                e.printStackTrace();
                return;
            }
    
            boolean matchFound = false;
    
            switch (choice) {
                case 1:
                    System.out.println("Match Found!");
                    for (String contact : contacts) {
                        String[] parts = contact.split(":");
                        String name = parts[0];
                        String number = parts[1];
                        if (name.toUpperCase().contains(searchTerm)) { // Compare uppercase name
                            System.out.println(name + ": " + number);
                            matchFound = true;
                        }
                    }
                    break;
                case 2:
                    System.out.println("Match Found!");
                    for (String contact : contacts) {
                        String[] parts = contact.split(":");
                        String name = parts[0];
                        String number = parts[1];
                        if (number.contains(searchTerm)) {
                            System.out.println(name + ": " + number);
                            matchFound = true;
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
    
            if (!matchFound) {
                System.out.println("No matching contacts found!");
            }
            
            // Inform the user to return to the main menu
            System.out.println("\nReturning to the main menu...");
            Menu();
        } catch (NoSuchElementException e) {
            System.out.println("Invalid input! Please enter a number.");
        }
    }
}