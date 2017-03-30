package tasz.mateusz;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;


import java.sql.*;
import java.io.*;

public class Main {

    public static void connect() {
        Connection conn = null;
        try {
            // db parametersC:\Users\taszzmat\Desktop\PGS\db.sqlite
            String url = "jdbc:sqlite:C:/Users/taszzmat/Desktop/PGS/db.sqlite";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();
    }
}
