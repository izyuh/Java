import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Bank {

    static Map<String, String> accounts = new HashMap<>();
    
    public static void main(String[] args) {

        
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Bank of Pomona");
        System.out.println("Enter 1 if you are already a member of 2 if you wish to open an account");
        int choice = scanner.nextInt();
        if (choice == 1) {
            handleLogin();
        } else if (choice == 2) {
            handleAccountCreation();
        } else {
            System.out.println("not valid choice");
        }
        
        scanner.close();
    }

    private static void handleLogin() {
        System.out.println("Please enter your username");
        
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            System.out.println("You entered " + username);

            System.out.println("Is this correct? Y/N");
            if(scanner.nextLine().toLowerCase().equals("y")) {
                break;
            } else {
                System.out.println("Please enter your username again");
                continue;
            }}

            
            while(true) {
                System.out.println("Please enter your password");
                Scanner scanner = new Scanner(System.in);
                String password = scanner.nextLine();
                System.out.println("Please re-enter your password");
                String confirmedPassword = scanner.nextLine();
                if(password.equals(confirmedPassword)) {
                    System.out.println("Password confirmed. Logging in...");
                    break;
                } else {
                    System.out.println("Passwords do not match. Try again.");
                    continue;   
                }
            }
            }

    private static void handleAccountCreation() {
        System.out.println("Please enter your desired username");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("You entered " + username);
        System.out.println("Is this correct? Y/N");
        if (scanner.nextLine().toLowerCase().equals("y")) {
            System.out.println("Please enter your password");
            String password = scanner.nextLine();
            System.out.println("Please re-enter your password");
            String confirmedPassword = scanner.nextLine();
            if (password.equals(confirmedPassword)) {
                String hashedPassword = hashPassword(password);

                System.out.println("Account created successfully with username: " + username + " and hashed password: " + hashedPassword);
            } else {
                System.out.println("Passwords do not match. Account creation failed.");
            }
        } else {
            System.out.println("Account creation cancelled.");
        }


}

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }





}