package tasz.mateusz;

import java.sql.SQLException;
import java.sql.*;
import java.util.Scanner;


/**
 * Class gives an access to SQLite database
 */
public class DataBaseHandler {

    /**
     * A connection field
     */
    public Connection conn = null;


    /**
     * A constructor. connect() function is run
     */
    public DataBaseHandler() {
        this.connect();
    }


    /**
     * A method connects to database
     */
    private void connect() {

        try {
            String url = "jdbc:sqlite::resource:db.sqlite";
            conn = DriverManager.getConnection(url);    // create a connection to the database
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * A method close connection to database
     */
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
