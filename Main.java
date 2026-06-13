import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final BookService bookService = new BookService();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   WELCOME TO LIBRARY MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = authMenu();
            } else {
                running = mainMenu();
            }
        }
        System.out.println("\nThank you for using the Library Management System. Goodbye!");
    }

    private static boolean authMenu() {
        System.out.println("\n---------- AUTHENTICATION ----------");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1": login(); break;
            case "2": register(); break;
            case "3": return false;
            default: System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        User user = userService.login(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("\nLogin successful! Welcome, " + user.getFullName() + ".");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Full Name: ");
        String fullName = sc.nextLine().trim();
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password (min 4 characters): ");
        String password = sc.nextLine().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.length() < 4) {
            System.out.println("All fields are required and password must be at least 4 characters.");
            return;
        }
        boolean success = userService.register(new User(username, password, fullName));
        if (success) {
            System.out.println("Registration successful! You can now log in.");
        } else {
            System.out.println("Username already exists. Please choose another.");
        }
    }

    private static boolean mainMenu() {
        System.out.println("\n---------- MAIN MENU ----------");
        System.out.println("Logged in as: " + currentUser.getFullName());
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search Book");
        System.out.println("4. Issue Book");
        System.out.println("5. Return Book");
        System.out.println("6. Delete Book");
        System.out.println("7. Edit / Update Book Information");
        System.out.println("8. Logout");
        System.out.print("Enter your choice: ");
        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1": bookService.addBook(sc); break;
            case "2": bookService.viewAllBooks(); break;
            case "3": bookService.searchBook(sc); break;
            case "4": bookService.issueBook(sc); break;
            case "5": bookService.returnBook(sc); break;
            case "6": bookService.deleteBook(sc); break;
            case "7": bookService.editBook(sc); break;
            case "8": currentUser = null; System.out.println("Logged out successfully."); break;
            default: System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }
}
