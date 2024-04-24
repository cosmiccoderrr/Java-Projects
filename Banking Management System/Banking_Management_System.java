import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.io.FileReader;
import java.nio.file.Paths;

public class Banking_Management_System {
    static Set<Long> generatedNumbers = new HashSet<>(); // Declare set here
    static Map<Long, Integer> accountBalances = new HashMap<>(); // Map to store account balances

    public static void main(String[] args) {
        loadAccountBalances(); // Load account balances from file
    
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
    
        while (!exit) {
            System.out.println("\t\t\t\t\t\tWELCOME TO THE BANK");
            System.out.println("\n");
            System.out.println("1. Sign In");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.println("Enter Your Choice");
    
            int choice = sc.nextInt();
    
            switch (choice) {
                case 1:
                    long accountNo = signin();
                    if (accountNo != 0) {
                        displayOptions(accountNo);
                    }
                    break;
                case 2:
                    signup();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
    

    // Function to log in (sign in)
    public static long signin() {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Enter Your AC No.");
            long accountno = sc.nextLong();
            System.out.println("Enter The PIN");
            int pin = sc.nextInt();

            boolean found = false;

            try {
                List<String> lines = Files.readAllLines(Paths.get("data.csv"));
                for (String line : lines) {
                    String[] parts = line.split(":");
                    if (parts.length >= 2) {
                        String label = parts[0].trim();
                        String value = parts[1].trim();
                        if (label.equals("Account Number") && Long.parseLong(value) == accountno) {
                            found = true;
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (found) {
                System.out.println("Congratulations! You are logged in.");
                return accountno;
            } else {
                System.out.println("\n");
                System.out.println("Account not found!");
                System.out.println("1. Sign In");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit");
                System.out.println("Enter Your Choice");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        break; // Continue the loop to sign in again
                    case 2:
                        signup();
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        }

        return 0; // Return 0 if the user chooses to exit
    }

    // Function to register yourself (sign up)
    public static void signup() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Your First Name:");
        String first_name = sc.nextLine();
        first_name = first_name.toUpperCase();

        System.out.println("Enter Your Last Name:");
        String last_name = sc.nextLine();
        last_name = last_name.toUpperCase();

        System.out.println("Enter Your Contact Number:");
        long contact_num = sc.nextLong();

        System.out.println("Enter Your Email:");
        sc.nextLine(); // Consume the newline character after nextLong()
        String email = sc.nextLine().toLowerCase();

        System.out.println("\nWhich Type of Account You Want To Open:");
        System.out.println("1. Savings");
        System.out.println("2. Current");
        System.out.println("Enter the number");
        int acc_auth = sc.nextInt();

        long accountNumber = get_acc_number();
        int pin = get_pin();
        System.out.println("\nCongratulations Your Account Has Been Opened");
        System.out.println("Your Account Number is :");
        System.out.println(accountNumber);
        System.out.println("Your Default PIN is :");
        System.out.println(pin);

        String acc_type = (acc_auth == 1) ? "Savings Account" : "Current Account";
        System.out.println("Your Account Type is " + acc_type);

        // Set initial balance to 0 for the new account
        accountBalances.put(accountNumber, 0);

        writeSignupInfoToCSV(first_name, last_name, contact_num, email, acc_auth, accountNumber, pin, acc_type);
    }

    // Write sign-up information to CSV file
    public static void writeSignupInfoToCSV(String first_name, String last_name, long contact_num, String email,
            int acc_auth, long accountNumber, int pin, String acc_type) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.csv", true))) {
            writer.write(String.format("Account Number: %d%n", accountNumber));
            writer.write(String.format("First Name: %s%n", first_name));
            writer.write(String.format("Last Name: %s%n", last_name));
            writer.write(String.format("Contact Number: %d%n", contact_num));
            writer.write(String.format("Email: %s%n", email));
            writer.write(String.format("Account Type: %s%n", acc_type));
            writer.write(String.format("PIN: %d%n", pin));
            writer.write("======================================="); // Separator
            writer.write("\n");
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to display account information
    public static void displayAccountInfo(long accountNumber) {
        System.out.println("Searching for account number: " + accountNumber);
        try {
            loadAccountBalances(); // Reload account balances from the CSV file
            BufferedReader br = new BufferedReader(new FileReader("data.csv"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String label = parts[0].trim();
                    String value = parts[1].trim();
                    if (label.equals("Account Number") && Long.parseLong(value) == accountNumber) {
                        found = true;
                        System.out.println("Account Information:");
                        System.out.println(line);
                        // Now print the rest of the information until the separator,
                        // excluding the PIN line
                        while ((line = br.readLine()) != null && !line.equals("=======================================")) {
                            if (!line.startsWith("PIN")) { // Exclude PIN information
                                System.out.println(line);
                            }
                        }
                        break;
                    }
                }
            }
            br.close(); // Close the BufferedReader
            if (!found) {
                System.out.println("Account not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to display options after sign in
    public static void displayOptions(long accountNumber) {
        Scanner sc = new Scanner(System.in);
        int money = accountBalances.get(accountNumber);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n1. Deposit Money");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Check Balance");
            System.out.println("4. Account Info");
            System.out.println("5. Exit");
            System.out.println("\n");
            System.out.println("Enter Your Choice");

            int auth1 = sc.nextInt();

            System.out.println("\n");
            System.out.println("Sure ! ");

            switch (auth1) {
                case 1:
                    System.out.println("How Much Amount You Want To Deposit");
                    int depositAmount = sc.nextInt();
                    money += depositAmount;
                    accountBalances.put(accountNumber, money);
                    System.out.println("Money Deposited");
                    System.out.println("The Bank Balance Is:");
                    System.out.println(money);
                    break;
                case 2:
                    System.out.println("How Much Amount You Want To Withdraw");
                    int withdrawAmount = sc.nextInt();
                    if (withdrawAmount <= money) {
                        money -= withdrawAmount;
                        accountBalances.put(accountNumber, money);
                        System.out.println("Money Withdrawn");
                    } else {
                        System.out.println("Insufficient balance!");
                    }
                    System.out.println("The Bank Balance Is:");
                    System.out.println(money);
                    break;
                case 3:
                    System.out.println("The Bank Balance Is:");
                    System.out.println(money);
                    break;
                case 4:
                    displayAccountInfo(accountNumber);
                    break;
                case 5:
                    System.out.println("Exit");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    // Functions for random data start from here
    public static int get_pin() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Generate a 4-digit random PIN
    }

    public static long get_acc_number() {
        Random random = new Random();
        long accountNumber;
        do {
            accountNumber = 1000000000L + random.nextInt(900000000);
        } while (generatedNumbers.contains(accountNumber));
        generatedNumbers.add(accountNumber); // Add generated number to the set
        return accountNumber;
    }

    // Load account balances from file
    public static void loadAccountBalances() {
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String label = parts[0].trim();
                    String value = parts[1].trim();
                    if (label.equals("Account Number")) {
                        long accountNumber = Long.parseLong(value);
                        // Set initial balance to 0 for each account
                        accountBalances.put(accountNumber, 0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}