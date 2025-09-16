import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ExpenseTrackerApp extends Application {

    private List<User> users;
    private User currentUser;
    private ObservableList<Expense> observableExpenses;

    private Label totalLabel;
    private ComboBox<String> userComboBox;
    private TableView<Expense> expenseTable;
    private TextField descriptionField, amountField, categoryField;

    @Override
    public void start(Stage primaryStage) {
        // Load data from CSV on startup
        users = ExpenseDataHandler.loadExpenses();
        if (users.isEmpty()) {
            // Create a default user if no data exists
            users.add(new User("Default User"));
        }
        currentUser = users.get(0);

        // GUI Components
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top section for user selection and total
        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(0, 0, 10, 0));
        userComboBox = new ComboBox<>();
        userComboBox.setPromptText("Select a User");
        userComboBox.setItems(FXCollections.observableArrayList(getUsernames()));
        userComboBox.getSelectionModel().select(currentUser.getUsername());
        userComboBox.setOnAction(e -> switchUser());

        Button newUserButton = new Button("New User");
        newUserButton.setOnAction(e -> createNewUser());

        totalLabel = new Label();
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        topBox.getChildren().addAll(userComboBox, newUserButton, new Separator(), totalLabel);
        root.setTop(topBox);

        // Center section for table view
        expenseTable = new TableView<>();
        TableColumn<Expense, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Expense, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Expense, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Expense, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        expenseTable.getColumns().addAll(dateCol, descriptionCol, amountCol, categoryCol);
        root.setCenter(expenseTable);

        // Bottom section for adding new expenses
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        descriptionField = new TextField();
        amountField = new TextField();
        categoryField = new TextField();
        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> addExpense());

        inputGrid.add(new Label("Description:"), 0, 0);
        inputGrid.add(descriptionField, 1, 0);
        inputGrid.add(new Label("Amount:"), 0, 1);
        inputGrid.add(amountField, 1, 1);
        inputGrid.add(new Label("Category:"), 0, 2);
        inputGrid.add(categoryField, 1, 2);
        inputGrid.add(addButton, 1, 3);
        
        root.setBottom(inputGrid);

        // Initial table and total update
        updateTableAndTotal();

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Personal Expense Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<String> getUsernames() {
        return users.stream().map(User::getUsername).toList();
    }
    
    private void switchUser() {
        String selectedUsername = userComboBox.getSelectionModel().getSelectedItem();
        if (selectedUsername != null) {
            for (User user : users) {
                if (user.getUsername().equals(selectedUsername)) {
                    currentUser = user;
                    updateTableAndTotal();
                    break;
                }
            }
        }
    }
    
    private void createNewUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New User");
        dialog.setHeaderText("Create a New User");
        dialog.setContentText("Enter username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            if (!username.trim().isEmpty()) {
                User newUser = new User(username.trim());
                users.add(newUser);
                userComboBox.setItems(FXCollections.observableArrayList(getUsernames()));
                userComboBox.getSelectionModel().select(username.trim());
                switchUser();
            }
        });
    }

    private void addExpense() {
        try {
            String description = descriptionField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            String category = categoryField.getText().trim();

            if (description.isEmpty() || amount <= 0 || category.isEmpty()) {
                throw new IllegalArgumentException("Invalid input.");
            }

            Expense newExpense = new Expense(description, amount, LocalDate.now(), category);
            currentUser.getExpenses().add(newExpense);
            
            // Update table and total
            observableExpenses.add(newExpense);
            updateTotal();

            // Clear fields
            descriptionField.clear();
            amountField.clear();
            categoryField.clear();

            // Save all data to CSV
            ExpenseDataHandler.saveExpenses(users);

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for the amount.");
        } catch (IllegalArgumentException e) {
            showAlert("Invalid Input", e.getMessage());
        }
    }

    private void updateTableAndTotal() {
        observableExpenses = FXCollections.observableArrayList(currentUser.getExpenses());
        expenseTable.setItems(observableExpenses);
        updateTotal();
    }

    private void updateTotal() {
        double total = currentUser.getExpenses().stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText(String.format("Total for %s: $%.2f", currentUser.getUsername(), total));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}