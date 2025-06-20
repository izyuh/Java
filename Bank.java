import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Bank {

    static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        accounts.put("admin", new Account(hashPassword("admin123"), 1000.0)); //// TEST ACCOUNT

        loadAccounts();
        homepage();

    }

    private static void homepage() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        String loggedInUser = null;

        System.out.println("Welcome to the Bank of Pomona");
        System.out.println("Enter 1 if you are already a member of 2 if you wish to open an account");

        choice = getValidatedIntInput(scanner, 1, 2);

        switch (choice) {
            case 1:
                loggedInUser = handleLogin();
                if (loggedInUser != null) {
                    runUserSession(loggedInUser);
                }
                break;
            case 2:
                handleAccountCreation();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                homepage();
        }

        scanner.close();
    }

    private static void runUserSession(String loggedInUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome " + loggedInUser + " to your account dashboard.");

        while (loggedInUser != null) {

            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Logout");

            double choice = getValidatedIntInput(scanner, 1, 4);

            if (choice == 1) {
                System.out.println("Your current balance is: " + accounts.get(loggedInUser).getBalance());
            } else if (choice == 2) {
                System.out.println("Please enter the amount you wish to deposit");
                double depositAmount = getValidatedDoubleInput(scanner, 0.01);
                accounts.get(loggedInUser).addBalance(depositAmount);
                saveAccountsToFile();
                System.out.println("You have successfully deposited " + depositAmount + ". Your new balance is: "
                        + accounts.get(loggedInUser).getBalance());
            } else if (choice == 3) {
                System.out.println("Please enter the amount you wish to withdraw");
                double withdrawAmount = getValidatedDoubleInput(scanner, 0.01);
                accounts.get(loggedInUser).withdrawBalance(withdrawAmount);
                saveAccountsToFile();
                System.out.println("You have successfully withdrawn " + withdrawAmount + ". Your new balance is: "
                        + accounts.get(loggedInUser).getBalance());
            } else if (choice == 4) {
                System.out.println("Logging out...");
                loggedInUser = null;
                homepage();
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static String handleLogin() {
        System.out.println("Please enter your username");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();

        System.out.println("Please enter your password");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);

        if (accounts.containsKey(username) && accounts.get(username).getHashedPassword().equals(hashedPassword)) {
            System.out.println("Logging in...");
            try {
                Thread.sleep(1000); // Simulate a delay for login processing
            } catch (InterruptedException e) {
            }
            System.out.println("Login successful as **" + username + "**\n");
            runUserSession(username);
            return username;
        } else {
            System.out.println(
                    "Login failed. Username or Password is incorrect.\n Try Again or press 1 to create an account.\n");
            if (scanner.hasNextInt() && scanner.nextInt() == 1) {
                handleAccountCreation();
            } else {
                handleLogin();
            }

            return null;
        }
    }

    private static void handleAccountCreation() {
        System.out.println("Please enter your desired username");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();

        if (accounts.containsKey(username)) {
            System.out.println("Username aleady exists in database. Please choose another username.");
            handleAccountCreation();

        }

        System.out.println("Please enter your password");
        String password = scanner.nextLine();
        System.out.println("Please confirm your password");
        String confirmedPassword = scanner.nextLine();
        if (password.equals(confirmedPassword)) {
            String hashedPassword = hashPassword(password);

            accounts.put(username, new Account(hashedPassword, 0.0));
            System.out.println("Account created successfully for " + username + "");
        } else {
            System.out.println("Passwords do not match. Account creation failed. Please try again.");
            handleAccountCreation();
        }

        System.out.println("You may now log in.\n");
        handleLogin();

    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private static void saveAccountsToFile() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter("accounts.txt")) {
            for (Map.Entry<String, Account> entry : accounts.entrySet()) {
                String username = entry.getKey();
                Account acc = entry.getValue();
                writer.println(username + "," + acc.getHashedPassword() + "," + acc.getBalance());
            }
        } catch (Exception e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    private static int getValidatedIntInput(Scanner scanner, int min, int max) {
        int value;
        while (true) {
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();

                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } else {
                System.out.println("Please enter a number within range");
                scanner.next();
            }
            scanner.nextLine();
        }
    }

    public static double getValidatedDoubleInput(Scanner scanner, double min) {
        double value;
        while (true) {
            if (scanner.hasNextDouble()) {
                value = scanner.nextDouble();

                if (value >= min) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and");
                }
            } else {
                System.out.println("Please enter a number within range");
                scanner.next();
            }
            scanner.nextLine();
        }
    }

    private static void loadAccounts() {
        java.io.File file = new java.io.File("accounts.txt");
        if (!file.exists())
            return;

        try (java.util.Scanner fileScanner = new java.util.Scanner(file)) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String username = parts[0];
                String hashedPassword = parts[1];
                double balance = Double.parseDouble(parts[2]);
                accounts.put(username, new Account(hashedPassword, balance));
            }
        } catch (Exception e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }

    }
}