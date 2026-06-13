import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BookService {
    private static final String DATA_DIR = "data";
    private static final String FILE = DATA_DIR + File.separator + "books.txt";
    private final Map<Integer, Book> books = new LinkedHashMap<>();
    private int nextBookId = 1;

    public BookService() {
        loadBooks();
    }

    private void loadBooks() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                Book b = Book.fromFileString(line);
                if (b != null) {
                    books.put(b.getBookId(), b);
                    if (b.getBookId() >= nextBookId) {
                        nextBookId = b.getBookId() + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
    }

    private void saveBooks() {
        new File(DATA_DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE), StandardCharsets.UTF_8))) {
            for (Book b : books.values()) {
                bw.write(b.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void addBook(Scanner sc) {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Author: ");
        String author = sc.nextLine().trim();
        System.out.print("Genre: ");
        String genre = sc.nextLine().trim();
        int copies = readInt(sc, "Total Copies: ");

        if (title.isEmpty() || author.isEmpty() || copies < 0) {
            System.out.println("All fields are required and copies must be a non-negative number.");
            return;
        }
        Book b = new Book(nextBookId++, title, author, genre, copies);
        books.put(b.getBookId(), b);
        saveBooks();
        System.out.println("Book added successfully with ID: " + b.getBookId());
    }

    public void viewAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        System.out.println("\n========== ALL BOOKS (" + books.size() + ") ==========");
        for (Book b : books.values()) System.out.println(b);
    }

    public void searchBook(Scanner sc) {
        System.out.println("\n--- Search Book ---");
        System.out.println("Search by: 1) ID  2) Title  3) Author  4) Genre");
        System.out.print("Choice: ");
        String ch = sc.nextLine().trim();
        List<Book> results = new ArrayList<>();
        switch (ch) {
            case "1": {
                int id = readInt(sc, "Enter Book ID: ");
                if (id < 0) { System.out.println("Invalid ID."); return; }
                Book b = books.get(id);
                if (b != null) results.add(b);
                break;
            }
            case "2": {
                System.out.print("Enter Title (or part of it): ");
                String q = sc.nextLine().trim().toLowerCase();
                for (Book b : books.values())
                    if (b.getTitle().toLowerCase().contains(q)) results.add(b);
                break;
            }
            case "3": {
                System.out.print("Enter Author (or part of it): ");
                String q = sc.nextLine().trim().toLowerCase();
                for (Book b : books.values())
                    if (b.getAuthor().toLowerCase().contains(q)) results.add(b);
                break;
            }
            case "4": {
                System.out.print("Enter Genre (or part of it): ");
                String q = sc.nextLine().trim().toLowerCase();
                for (Book b : books.values())
                    if (b.getGenre().toLowerCase().contains(q)) results.add(b);
                break;
            }
            default:
                System.out.println("Invalid choice.");
                return;
        }
        if (results.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            System.out.println("\n--- Search Results (" + results.size() + ") ---");
            for (Book b : results) System.out.println(b);
        }
    }

    public void issueBook(Scanner sc) {
        System.out.println("\n--- Issue Book ---");
        int id = readInt(sc, "Enter Book ID to issue: ");
        if (id < 0) { System.out.println("Invalid ID."); return; }
        Book b = books.get(id);
        if (b == null) { System.out.println("Book not found."); return; }
        if (b.getAvailableCopies() <= 0) {
            System.out.println("No copies currently available for this book.");
            return;
        }
        b.setAvailableCopies(b.getAvailableCopies() - 1);
        saveBooks();
        System.out.println("Book issued: " + b.getTitle());
        System.out.println("Remaining available copies: " + b.getAvailableCopies());
    }

    public void returnBook(Scanner sc) {
        System.out.println("\n--- Return Book ---");
        int id = readInt(sc, "Enter Book ID to return: ");
        if (id < 0) { System.out.println("Invalid ID."); return; }
        Book b = books.get(id);
        if (b == null) { System.out.println("Book not found."); return; }
        if (b.getAvailableCopies() >= b.getTotalCopies()) {
            System.out.println("All copies of this book are already in the library.");
            return;
        }
        b.setAvailableCopies(b.getAvailableCopies() + 1);
        saveBooks();
        System.out.println("Book returned: " + b.getTitle());
        System.out.println("Available copies now: " + b.getAvailableCopies());
    }

    public void deleteBook(Scanner sc) {
        System.out.println("\n--- Delete Book ---");
        int id = readInt(sc, "Enter Book ID to delete: ");
        if (id < 0) { System.out.println("Invalid ID."); return; }
        Book b = books.remove(id);
        if (b == null) { System.out.println("Book not found."); return; }
        saveBooks();
        System.out.println("Book deleted: " + b.getTitle());
    }

    public void editBook(Scanner sc) {
        System.out.println("\n--- Edit Book ---");
        int id = readInt(sc, "Enter Book ID to edit: ");
        if (id < 0) { System.out.println("Invalid ID."); return; }
        Book b = books.get(id);
        if (b == null) { System.out.println("Book not found."); return; }

        System.out.println("Current details: " + b);

        System.out.print("New Title (leave empty to keep current): ");
        String t = sc.nextLine().trim();
        if (!t.isEmpty()) b.setTitle(t);

        System.out.print("New Author (leave empty to keep current): ");
        String a = sc.nextLine().trim();
        if (!a.isEmpty()) b.setAuthor(a);

        System.out.print("New Genre (leave empty to keep current): ");
        String g = sc.nextLine().trim();
        if (!g.isEmpty()) b.setGenre(g);

        System.out.print("New Total Copies (leave empty to keep current): ");
        String tc = sc.nextLine().trim();
        if (!tc.isEmpty()) {
            try {
                int newTotal = Integer.parseInt(tc);
                if (newTotal < 0) {
                    System.out.println("Total copies cannot be negative.");
                    return;
                }
                int issued = b.getIssuedCopies();
                if (newTotal < issued) {
                    System.out.println("New total cannot be less than currently issued copies (" + issued + ").");
                    return;
                }
                b.setTotalCopies(newTotal);
                b.setAvailableCopies(newTotal - issued);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Update cancelled.");
                return;
            }
        }
        saveBooks();
        System.out.println("Book updated successfully.");
        System.out.println("Updated: " + b);
    }
}
