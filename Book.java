public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private int totalCopies;
    private int availableCopies;

    public Book(int bookId, String title, String author, String genre, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public Book(int bookId, String title, String author, String genre,
                int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public int getIssuedCopies() {
        return totalCopies - availableCopies;
    }

    public String toFileString() {
        return bookId + "|"
                + title.replace("|", "/") + "|"
                + author.replace("|", "/") + "|"
                + genre.replace("|", "/") + "|"
                + totalCopies + "|"
                + availableCopies;
    }

    public static Book fromFileString(String line) {
        if (line == null || line.isEmpty()) return null;
        String[] parts = line.split("\\|", -1);
        if (parts.length == 6) {
            try {
                return new Book(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        Integer.parseInt(parts[4]),
                        Integer.parseInt(parts[5])
                );
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %-4d | Title: %-25s | Author: %-18s | Genre: %-12s | Total: %-3d | Available: %-3d",
                bookId, title, author, genre, totalCopies, availableCopies);
    }
}
