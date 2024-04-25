/* Project:- Airline Reservation System
   Developer:- Kartik Verma
   Date:-16-03-2024 
   Time:- 15:52
   Day:- Saturday
*/ 

import java.util.*;
import java.io.*;

public class ARS {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\t\t\t\t\t\tWELCOME TO THE AIRLINE RESERVATION SYSTEM");
        boolean exit = false;
        while (!exit) {
            System.out.println("\n1. Sign In");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.println("Enter Your Choice:");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    signIn();
                    break;
                case 2:
                    signUp();
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



    public static void signIn() {
        System.out.println("Enter Your Username:");
        String username = sc.nextLine();

        System.out.println("Enter Your Password:");
        String password = sc.nextLine();

        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader("User_Data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length >= 2 && userInfo[5].equals(username) && userInfo[6].equals(password)) {
                    found = true;
                    System.out.println("Login Success!");
                    displayOptions();
                    break;
                }
            }
            if (!found) {
                System.out.println("Invalid username or password. Please try again.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static void signUp() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("User_Data.csv", true))) {
            System.out.println("Enter Your First Name:");
            String firstName = sc.nextLine();

            System.out.println("Enter Your Last Name:");
            String lastName = sc.nextLine();

            System.out.println("Enter Your Country:");
            String country = sc.nextLine();

            System.out.println("Enter Your Contact Number:");
            String contactNo = sc.nextLine();

            System.out.println("Enter Your Email:");
            String email = sc.nextLine();

            System.out.println("Enter Your Username:");
            String username = sc.nextLine();

            System.out.println("Enter Your Password:");
            String password = sc.nextLine();

            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s%n", firstName, lastName, country, contactNo, email, username, password));
            System.out.println("You are registered now. Congratulations!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public static void displayOptions() {
        boolean exitMenu = false;
        while (!exitMenu) {
            System.out.println("\n1. Book a Flight Ticket");
            System.out.println("2. Check Your Ticket info");
            System.out.println("3. Check Flight info");
            System.out.println("4. Check Your Account info");
            System.out.println("5. Exit");
            System.out.println("Enter Your Choice:");
    
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    bookFlightTicket();
                    break;
                case 2:
                    checkTicketInfo();
                    break;
                case 3:
                    checkFlightInfo();
                    break;
                case 4:
                    checkAccountInfo();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    exitMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }




    
    public static void checkTicketInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader(getUserTicketFileName()))) {
            String line;
            System.out.println("Your Ticket Information:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Ticket information not found.");
        }
    }





    public static void checkFlightInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader("Aviation_data.csv"))) {
            String line;
            System.out.println("\nFlight Information:");
            System.out.println("Flight No.\tFlight Name\tRoute\tTime\tSeats Available");
    
            while ((line = br.readLine()) != null) {
                String[] flightInfo = line.split(",");
                if (flightInfo.length >= 5) {
                    System.out.println(flightInfo[0] + "\t" + flightInfo[1] + "\t" + flightInfo[2] + "\t" + flightInfo[3] + "\t" + flightInfo[4]);
                } else {
                    System.out.println("Flight data format is incorrect. Unable to display flight info.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private static void printFlightInfo(String flightData) {
        String[] flightInfo = flightData.split(",");
        if (flightInfo.length >= 5) {
            try {
                String flightUID = flightInfo[0].trim();
                String flightName = flightInfo[1].trim();
                String route = flightInfo[2].trim();
                String time = flightInfo[3].trim();
                String seatsAvailable = flightInfo[4].trim();
    
                System.out.println("Flight No.\tFlight Name\tRoute\tTime\tSeats Available");
                System.out.println(flightUID + "\t" + flightName + "\t" + route + "\t" + time + "\t" + seatsAvailable);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Flight data format is incorrect. Unable to display flight info.");
            }
        } else {
            System.out.println("Flight data format is incorrect. Unable to display flight info.");
        }
    }





    public static void checkAccountInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader("User_Data.csv"))) {
            String line;
            System.out.println("\nYour Account Information:");
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length >= 2) {
                    System.out.println("First Name: " + userInfo[0]);
                    System.out.println("Last Name: " + userInfo[1]);
                    System.out.println("Country: " + userInfo[2]);
                    System.out.println("Contact Number: " + userInfo[3]);
                    System.out.println("Email: " + userInfo[4]);
                    System.out.println("Username: " + userInfo[5]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static String getUserTicketFileName() {
        String firstName = "";
        try (BufferedReader br = new BufferedReader(new FileReader("User_Data.csv"))) {
            String line;
            if ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length >= 1) {
                    firstName = userInfo[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format("%s_Ticket.csv", firstName);
    }





    public static void bookFlightTicket() {
        try (BufferedReader br = new BufferedReader(new FileReader("Aviation_data.csv"))) {
            String line;
            System.out.println("\nFlights Available for Booking:");
            System.out.println(String.format("%-15s%-20s%-20s%-15s%-20s", "Flight No.", "Flight Name", "Route", "Time", "Available Seats"));
            while ((line = br.readLine()) != null) {
                String[] flightInfo = line.split(",");
                if (flightInfo.length >= 5) {
                    System.out.println(String.format("%-15s%-20s%-20s%-15s%-20s", flightInfo[0], flightInfo[1], flightInfo[2], flightInfo[3], flightInfo[4]));
                }
            }
    
            System.out.println("\nEnter the Flight UID to Book:");
            String flightUID = sc.nextLine();
    
            // Display flight information based on UID
            displayFlightInfo(flightUID);
    
            System.out.println("\nEnter Your First Name:");
            String firstName = sc.nextLine();
    
            System.out.println("Enter Your Last Name:");
            String lastName = sc.nextLine();
    
            System.out.println("Enter Your Contact Number:");
            String contactNo = sc.nextLine();
    
            System.out.println("\nSelect Seat Type:");
            System.out.println("1. Economy Class");
            System.out.println("2. Business Class");
            System.out.println("Enter Your Choice:");
            int seatType = sc.nextInt();
            sc.nextLine(); // Consume newline
    
            int price = (seatType == 1) ? 2000 : 4000;
    
            System.out.println("\nEnter the Number of Seats to Book:");
            int numSeats = sc.nextInt();
            sc.nextLine(); // Consume newline
    
            int totalAmount = numSeats * price;
            double gst = totalAmount * 0.18;
            double totalAmountWithGST = totalAmount + gst;
    
            System.out.println("\nTotal Amount to be Paid: Rs. " + totalAmountWithGST);
            System.out.println("Payment Done? (yes/no)");
            String paymentStatus = sc.nextLine();
            if (paymentStatus.equalsIgnoreCase("yes")) {
                System.out.println("Payment Successful!");
                System.out.println("Ticket successfully booked. Your Ticket is saved.");
                String fileName = String.format("%s_Ticket.csv", getUserFirstName());
                try (BufferedWriter ticketWriter = new BufferedWriter(new FileWriter(fileName))) {
                    ticketWriter.write(String.format("%-20s%-15s%-10s%-10s%-15s%-15s%-15s%-15s%n",
                            "Flight UID", "Flight Name", "Flight Time", "Num Seats", "Total Amount", "First Name", "Last Name", "Contact No.")); // Header
                    String flightInfo = getFlightInfo(flightUID);
                    String[] flightData = flightInfo.split(",");
                    ticketWriter.write(String.format("%-20s%-15s%-10s%-10s%-15s%-15s%-15s%-15s%n",
                            flightData[1], flightData[0], flightData[2], numSeats, totalAmountWithGST, firstName, lastName, contactNo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Payment not done. Please complete the payment process.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static String getUserFirstName() {
        String firstName = "";
        try (BufferedReader br = new BufferedReader(new FileReader("User_Data.csv"))) {
            String line;
            if ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length >= 1) {
                    firstName = userInfo[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstName;
    }





    public static void displayFlightInfo(String flightUID) {
        try (BufferedReader br = new BufferedReader(new FileReader("Aviation_data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] flightInfo = line.split(",");
                if (flightInfo.length >= 5 && flightInfo[0].equals(flightUID)) {
                    System.out.println("\nFlight Information:");
                    System.out.println("Flight Name: " + flightInfo[1]);
                    System.out.println("Route: " + flightInfo[2]);
                    System.out.println("Time: " + flightInfo[3]);
                    System.out.println("Available Seats: " + flightInfo[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    
    public static String getFlightInfo(String flightUID) {
        try (BufferedReader br = new BufferedReader(new FileReader("Aviation_data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] flightInfo = line.split(",");
                if (flightInfo.length >= 5 && flightInfo[0].equals(flightUID)) {
                    return flightInfo[1] + "," + flightInfo[0] + "," + flightInfo[2] + "," + flightInfo[3];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}