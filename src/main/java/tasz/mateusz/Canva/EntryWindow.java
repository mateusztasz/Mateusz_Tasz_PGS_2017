package tasz.mateusz.Canva;

/**
 * Created by Mateusz on 2017-03-31.
 */

import java.sql.*;
import java.util.Scanner;

import tasz.mateusz.DataBaseHandler;
import tasz.mateusz.Text;

public class EntryWindow extends Canva {
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

    @Override
    public void showMenu() {

        System.out.println();
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

    }

    @Override
    public Canva perform() {

        String command;
        System.out.println("Enter Your choice: ");
        System.out.print("$>");
        Scanner scan = new Scanner(System.in); //obiekt do odebrania danych od użytkownika

        command = scan.nextLine().trim();

        if (validate(command, "login")) {
            int userId = db.login();

            if (userId > 0) return new MainWindow(this.db, userId);
            else    return this;

        } else if (validate(command, "create user")) {
            createUser();
            return this;
        } else if (validate(command, "list users")) {
            this.db.listCustomers();
        } else if (validate(command, "help")) {
            showHelp();
        } else if (validate(command, "exit")) {
            return new ExitWindow();
        } else {
          //  System.out.println("Command not recognized");
        }
        return this;
    }



    public void showHelp() {

        System.out.println();
        System.out.println("=============HELP===========");
        System.out.println("Explanation of each option:");
        System.out.println("  -login            Log into database in order to work with application.");
        System.out.println("  -create user      Create a new user in case you are a new one.");
        System.out.println("  -list users       Check all users in database");
        System.out.println("  -help             Print this help information.");
        System.out.println("  -exit             Close application.");


    }

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
            System.out.println();
            System.out.println("Error occured:");
            System.out.println(e.getMessage());
            System.out.println("A creating process has been stopped.");
            System.out.println();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
