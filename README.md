# Simple-Expense-Tracker
This is a simple, modern Expense Tracker application built with Java and JavaFX. It allows users to track expenses, categorize them, and view their total spending. The application supports multiple users and stores all data persistently in a CSV file. 

Features
Multi-User Support: Create and manage expenses for different individuals.

Persistent Data Storage: All expense data is automatically saved to an expenses.csv file, ensuring no data is lost between sessions.

Modern GUI: A clean and intuitive user interface built with JavaFX.

Expense Management: Add new expenses with details like description, amount, date, and category.

Total Expenses: View the total amount spent for the current user.

Getting Started
Prerequisites
Java Development Kit (JDK) 11 or higher

JavaFX SDK: Since JavaFX is not included in the JDK anymore, you will need to download it separately. Alternatively, you can use a build tool like Maven.

Running the Application
There are two primary ways to run this project:

Method 1: Command-Line (without a build tool)
Download and Unzip the JavaFX SDK.

Navigate to the project directory in your terminal.

Compile the Java files using the following command, replacing "PATH_TO_FX" with the actual path to your JavaFX SDK's lib directory:

javac --module-path "PATH_TO_FX\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics Expense.java User.java ExpenseDataHandler.java ExpenseTrackerApp.java

Run the application with the following command:

java --module-path "PATH_TO_FX\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics ExpenseTrackerApp

Method 2: Using Maven (Recommended)
Ensure you have Maven installed and set up.

Create a Maven project and place the Java files in src/main/java.

Add the necessary JavaFX dependencies and the maven-compiler-plugin to your pom.xml file.

Run the application directly from the terminal using the Maven command:

Bash

mvn javafx:run
File Structure
ExpenseTrackerApp.java: The main application class containing the JavaFX GUI logic.

User.java: The data model for a user, containing a list of their expenses.

Expense.java: The data model for a single expense entry.

ExpenseDataHandler.java: A utility class for handling the reading and writing of data to the expenses.csv file.

expenses.csv: The file where all user and expense data is stored. This file is automatically created and managed by the application.
