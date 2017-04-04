package tasz.mateusz.Canva;

/**
 * Created by Mateusz on 2017-03-31.
 */

import java.sql.*;
import java.util.Scanner;
import java.util.*;

import tasz.mateusz.DataBaseHandler;
import tasz.mateusz.TextManipulation.Color;

public class EntryWindow extends AbstractWindow {
    /**
     * Database handler for this specific window.
     */
    private DataBaseHandler db;

    private static final String CUSTOMER_CREATE_NEW =
            "INSERT INTO CUSTOMER(Login, Pass, Name, Surname, Address, Phone) VALUES(?,?,?,?,?,?);";
    private PreparedStatement customerCreateNewStatement;

    public EntryWindow(DataBaseHandler db) {
        this(db, null);
    }

    private EntryWindow(DataBaseHandler db, String name) {
        this.db = db;
        showMenu();
    }


    /**
     * Method shows menu for EntryWindow (not logged in user)
     */
    @Override
    public void showMenu() {

        System.out.println(Color.CYAN);
        System.out.println("============================");
        System.out.println("|         WELCOME          |");
        System.out.println("============================");
        System.out.println("|                          |");
        System.out.println("|       $> login           |");
        System.out.println("|       $> create user     |");
        System.out.println("|       $> list users      |");
        System.out.println("|       $> help            |");
        System.out.println("|       $> exit            |");
        System.out.println("============================");
        System.out.print(Color.RESET);

    }


    /**
     * Method performs action for EntryWindow (not logged in user)
     */
    @Override
    public AbstractWindow perform() {

        String command;
        System.out.println("Enter Your choice: ");
        System.out.print("$>");
        Scanner scan = new Scanner(System.in); //obiekt do odebrania danych od użytkownika

        command = scan.nextLine().trim();

        if (validate(command, "login")) {
            int userId = this.login();

            if (userId > 0) return new MainWindow(this.db, userId);
            else return this;

        } else if (validate(command, "create user")) {
            createUser();
            return this;
        } else if (validate(command, "list users")) {
            this.listCustomers();
        } else if (validate(command, "help")) {
            showHelp();
        } else if (validate(command, "exit")) {
            return new ExitWindow();
        }

        return this;
    }


    /**
     * @return userId User identification from database if logged in. Otherwise returns -1
     */
    private int login() {
        int userId = -1;
        String sqlFindCustomer = "select * from CUSTOMER where Login = ? and Pass = ?;";
        String sqlCustomerStar = "select * from CUSTOMER where CustomerId = ?;";
        ResultSet resultSetFoundCustomer;
        ResultSet resultSetCustomerStar;

        String login, pass;
        String name = "", surname = "";

        System.out.print("login: ");
        Scanner scanLogin = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
        login = scanLogin.nextLine();

        System.out.print("pass: ");
        Scanner scanPass = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
        pass = scanPass.nextLine();

        try {

            // Look for a customer
            resultSetFoundCustomer = db.executeQuery(sqlFindCustomer, login, pass);

            // Localizes customer by its Id number
            if (resultSetFoundCustomer.next()) {
                userId = resultSetFoundCustomer.getInt(1);
            }

            // Get name of logged in customer
            resultSetCustomerStar = db.executeQuery(sqlCustomerStar, Integer.toString(userId));

            if (resultSetCustomerStar.next()) {
                name = resultSetCustomerStar.getString("Name");
                surname = resultSetCustomerStar.getString("surname");
            }

            // Print info about success
            if (userId > 0) {
                System.out.print(Color.GREEN);
                System.out.println("Logged in successfully as " + name + " " + surname);
                System.out.print(Color.RESET);
            } else {
                System.out.print(Color.RED);
                System.out.println("Your login and password do not match.");
                System.out.print(Color.RESET);
            }

            return userId;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userId;
    }


    /**
     * Method list all Customers from databate. Prints only name and surname.
     */
    private void listCustomers() {
        String sql = "SELECT * FROM CUSTOMER;";
        ResultSet resultSet;

        try {

            resultSet = db.executeQuery(sql);

            System.out.println(Color.YELLOW);
            System.out.println("List of all available customers. If you are one of them you may log in.");
            System.out.print(Color.RESET);

            while (resultSet.next()) {

                System.out.print(resultSet.getString("CustomerId"));
                System.out.print(". ");
                System.out.print(resultSet.getString("Name"));
                System.out.print(" ");
                System.out.println(resultSet.getString("Surname"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method prints a help manual
     */
    private void showHelp() {

        System.out.println(Color.PURPLE);
        System.out.println("=============HELP===========");
        System.out.println("Explanation of each option:");
        System.out.println("  -login            Log into database in order to work with application.");
        System.out.println("  -create user      Create a new user in case you are a new one.");
        System.out.println("  -list users       Check all users in database.");
        System.out.println("  -menu             Display a menu.");
        System.out.println("  -help             Print this help information.");
        System.out.println("  -exit             Close application.");
        System.out.print(Color.RESET);

    }


    /**
     * Method create a new user. Read proper data from input buffer and execute proper query to
     * add row in database.
     * Does not log in!
     */
    private void createUser() {
        String sqlCreateUser = "insert into CUSTOMER(Login, Pass, Name, Surname, Address, Phone) values(?,?,?,?,?,?);";
        String sqlCustomer = "SELECT * FROM CUSTOMER;";

        ResultSet resultSetCustomer;
        ResultSetMetaData resultSetCustomer_MetaData;

        // Stores important data from user to procceed them to database handler
        Map<String, Object> map = new HashMap<String, Object>();

        // Takes data from user
        Scanner scanLogin = new Scanner(System.in);

        try {
            // Get meta data about columns name
            resultSetCustomer = db.executeQuery(sqlCustomer);
            resultSetCustomer_MetaData = resultSetCustomer.getMetaData();

            // Enter data to add into database
            for (int i = 2; i < resultSetCustomer_MetaData.getColumnCount() + 1; i++) {

                System.out.print(resultSetCustomer_MetaData.getColumnName(i) + " :");
                map.put(resultSetCustomer_MetaData.getColumnName(i), scanLogin.nextLine());
            }

            System.out.println();

            // Add row into database
            db.executeUpdate(sqlCreateUser, map);

            System.out.print(Color.GREEN);
            System.out.println("You have created a new user successfully.");
            System.out.print(Color.RESET);

        } catch (NumberFormatException e) {
            System.out.println(Color.RED);
            System.out.println("Error occurred:");
            System.out.println(e.getMessage());
            System.out.println("A creating process has been stopped. Check your phone number.");
            System.out.println(Color.RESET);
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

}
