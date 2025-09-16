import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDataHandler {
    private static final String FILE_NAME = "expenses.csv";

    public static void saveExpenses(List<User> users) {
        try (FileWriter writer = new FileWriter(FILE_NAME);
             BufferedWriter bw = new BufferedWriter(writer)) {
            // Write header
            bw.write("username,date,description,amount,category\n");

            // Write each user's expenses
            for (User user : users) {
                for (Expense expense : user.getExpenses()) {
                    String line = String.format("%s,%s,%s,%.2f,%s\n",
                        user.getUsername(),
                        expense.getDate(),
                        expense.getDescription().replace(",", ""), // Remove commas to prevent breaking the CSV
                        expense.getAmount(),
                        expense.getCategory().replace(",", ""));
                    bw.write(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> loadExpenses() {
        List<User> users = new ArrayList<>();
        try (FileReader reader = new FileReader(FILE_NAME);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String username = parts[0];
                    LocalDate date = LocalDate.parse(parts[1]);
                    String description = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String category = parts[4];
                    
                    // Find or create user
                    User user = findUser(users, username);
                    if (user == null) {
                        user = new User(username);
                        users.add(user);
                    }
                    user.getExpenses().add(new Expense(description, amount, date, category));
                }
            }
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
        return users;
    }

    private static User findUser(List<User> users, String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}