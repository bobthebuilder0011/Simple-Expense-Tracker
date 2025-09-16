import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<Expense> expenses;

    public User(String username) {
        this.username = username;
        this.expenses = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}