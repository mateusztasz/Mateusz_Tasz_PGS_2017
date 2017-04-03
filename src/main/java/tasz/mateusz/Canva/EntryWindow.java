package tasz.mateusz.Canva;

/**
 * Created by Mateusz on 2017-03-31.
 */

import java.sql.*;
import java.util.Scanner;

import tasz.mateusz.TextManipulation.Color;
import tasz.mateusz.DataBaseHandler;

public class EntryWindow extends AbstractWindow {
    /**
     * Database handler for this specific window.
     */
    private DataBaseHandler db;

    private static final String CUSTOMER_CREATE_NEW =
            "insert into CUSTOMER(Login, Pass, Name, Surname, Address, Phone) values(?,?,?,?,?,?);";
    private PreparedStatement customerCreateNewStatement;

    public EntryWindow(DataBaseHandler db) {
        this(db, null);
    }

    public EntryWindow(DataBaseHandler db, String name) {
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
     *
     * @return userId User identification from database if logged in. Otherwise returns -1
     */
    private int login(){
        int userId = -1;
        String CUSTOMER_LOGIN_SQL =
                "select * from CUSTOMER where Login = ? and Pass = ?;";
        PreparedStatement customerLoginStatement;

        String CUSTOMER_NAME =
                "select * from CUSTOMER where CustomerId = ?;";
        PreparedStatement customerNameStatement;

        try {

            String login="",pass="";
            String name="",surname="";

            System.out.print("login: ");
            Scanner scanLogin = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
            login = scanLogin.nextLine();

            System.out.print("pass: ");
            Scanner scanPass = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
            pass = scanPass.nextLine();

            // Look for a customer
            customerLoginStatement = db.conn.prepareStatement(CUSTOMER_LOGIN_SQL);
            customerLoginStatement.clearParameters();
            customerLoginStatement.setString(1, login);
            customerLoginStatement.setString(2, pass);
            ResultSet resultSet = customerLoginStatement.executeQuery();

            // Localizes customer by its Id number
            if (resultSet.next()){
                userId = resultSet.getInt(1);
            }

            // Get name of logged in customer
            customerNameStatement = db.conn.prepareStatement(CUSTOMER_NAME);
            customerNameStatement.clearParameters();
            customerNameStatement.setString(1, Integer.toString(userId));
            resultSet = customerLoginStatement.executeQuery();
            if (resultSet.next()){
                name = resultSet.getString("Name");
                surname = resultSet.getString("surname");
            }

            // Print info about success
            if(userId>0) {
                System.out.print(Color.GREEN);
                System.out.println("Logged in successfully as "+name+" "+surname);
                System.out.print(Color.RESET);
            }
            else {
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
        try {
            Statement stmt = db.conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM CUSTOMER;");

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
    private void createUser(){
        try {
            String inputString;
            int inputInt;
            Scanner scanLogin = new Scanner(System.in); //obiekt do odebrania danych od użytkownika

            // Prepare statement
            customerCreateNewStatement = db.conn.prepareStatement(CUSTOMER_CREATE_NEW);
            customerCreateNewStatement.clearParameters();

            // Get meta data about columns name
            Statement stmt = db.conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM CUSTOMER;");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();


            System.out.println("Col: "+ columnsNumber);

            // Enter data to add into database
            for(int i = 2; i < columnsNumber+1; i++) {
                System.out.print(resultSetMetaData.getColumnName(i) + " :");
                if(resultSetMetaData.getColumnTypeName(i).equals("VARCHAR")){
                    inputString = scanLogin.nextLine();
                    customerCreateNewStatement.setString(i-1, inputString);
                }
                else if(resultSetMetaData.getColumnTypeName(i).equals("INT")){

                    inputInt = Integer.parseInt(scanLogin.nextLine());
                    customerCreateNewStatement.setInt(i-1, inputInt);
                }

            }
            System.out.println();

            // Add row into database
            customerCreateNewStatement.executeUpdate();

        }
        catch (NumberFormatException e){
            System.out.println(Color.RED);
            System.out.println("Error occured:");
            System.out.println(e.getMessage());
            System.out.println("A creating process has been stopped.");
            System.out.println(Color.RESET);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
