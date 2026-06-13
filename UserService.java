import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final String DATA_DIR = "data";
    private static final String FILE = DATA_DIR + File.separator + "users.txt";
    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        loadUsers();
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return input;
        }
    }

    private void loadUsers() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                User u = User.fromFileString(line);
                if (u != null) users.put(u.getUsername(), u);
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        new File(DATA_DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE), StandardCharsets.UTF_8))) {
            for (User u : users.values()) {
                bw.write(u.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public boolean register(User user) {
        if (users.containsKey(user.getUsername())) return false;
        user.setPassword(hash(user.getPassword()));
        users.put(user.getUsername(), user);
        saveUsers();
        return true;
    }

    public User login(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(hash(password))) return u;
        return null;
    }
}
