package tasz.mateusz;

import java.sql.SQLException;
import java.sql.*;
import java.util.Scanner;


/**
 * Created by Mateusz on 2017-03-31.
 */
public class DataBaseHandler {

    private static final String CUSTOMER_LOGIN_SQL =
            "select * from CUSTOMER where Login = ? and Pass = ?;";
    private PreparedStatement customerLoginStatement;

    private static final String CUSTOMER_NAME =
            "select * from CUSTOMER where CustomerId = ?;";
    private PreparedStatement customerNameStatement;

    private static final String CUSTOMER_CREATE_NEW =
            "insert into CUSTOMER(Login, Pass, Name, Surname, Address, Phone) values(?,?,?,?,?,?);";
    private PreparedStatement customerCreateNewStatement;

    public Connection conn = null;


    public DataBaseHandler() {
        this.connect();
    }

    public void connect() {

        try {
            String url = "jdbc:sqlite::resource:db.sqlite";
            conn = DriverManager.getConnection(url);    // create a connection to the database
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listCustomers() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM CUSTOMER;");

            System.out.println();
            System.out.println("List of all available customers. If you are one of them you may log in.");
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


    public int login(){
        int userId = -1;
        try {

            String login,pass;
            String name="",surname="";
            System.out.print("login: ");

            Scanner scanLogin = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
            login = scanLogin.nextLine();

            System.out.print("pass: ");
            Scanner scanPass = new Scanner(System.in); //obiekt do odebrania danych od użytkownika
            pass = scanPass.nextLine();

            // Look for a customer
            customerLoginStatement = conn.prepareStatement(CUSTOMER_LOGIN_SQL);
            customerLoginStatement.clearParameters();
            customerLoginStatement.setString(1, login);
            customerLoginStatement.setString(2, pass);
            ResultSet resultSet = customerLoginStatement.executeQuery();

            // Localizes customer by its Id number
            if (resultSet.next()){
                userId = resultSet.getInt(1);
            }

            // Get name of logged in customer
            customerNameStatement = conn.prepareStatement(CUSTOMER_NAME);
            customerNameStatement.clearParameters();
            customerNameStatement.setString(1, Integer.toString(userId));
            resultSet = customerLoginStatement.executeQuery();
            if (resultSet.next()){
                name = resultSet.getString("Name");
                surname = resultSet.getString("surname");
            }

            // Print info about success
            if(userId>0) System.out.println("Logged in successfully as "+name+" "+surname);
            else System.out.println("Your login and password do not match.");

            return userId;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userId;
    }

}
