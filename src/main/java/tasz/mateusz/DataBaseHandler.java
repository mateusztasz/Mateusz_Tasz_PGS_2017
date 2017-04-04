package tasz.mateusz;

/**
 * Created by Mateusz on 03.04.2017.
 */


import tasz.mateusz.TextManipulation.Color;

import java.sql.*;
import java.util.Map;

/**
 * Class gives an access to SQLite database
 */
public class DataBaseHandler {

    /**
     * A connection field
     */
    public Connection conn = null;


    /**
     * A constructor. Connects to the database
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

    public ResultSet executeQuery(String sql, String...args) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.clearParameters();
        int i = 1;

        for (String elem : args) {
            stmt.setString(i++, elem);
        }
        return stmt.executeQuery();
    }


    public ResultSet executeQuery(String sql) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public ResultSet executeQuery(String sql, int...args) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.clearParameters();
        int i = 1;

        for (int elem : args) {
            stmt.setInt(i++, elem);
        }
        return stmt.executeQuery();
    }


    public int executeUpdate(String sql, int arg1, int arg2, String arg3) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.clearParameters();
        stmt.setInt(1, arg1);
        stmt.setInt(2, arg2);
        stmt.setString(3, arg3);

        return stmt.executeUpdate();
    }

    public int executeUpdate(String sql,String arg1, float arg2, int arg3, int arg4) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.clearParameters();
        stmt.setString(1,arg1);
        stmt.setFloat(2, arg2);
        stmt.setInt(3, arg3);
        stmt.setInt(4, arg4);


        return stmt.executeUpdate();
    }


    public int executeUpdate(String sql, Map<String, Object> map) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.clearParameters();

        stmt.setString(1, map.get("Login").toString());
        stmt.setString(2, map.get("Pass").toString());
        stmt.setString(3, map.get("Name").toString());
        stmt.setString(4, map.get("Surname").toString());
        stmt.setString(5, map.get("Address").toString());
        stmt.setInt   (6, Integer.parseInt(map.get("Phone").toString()));

        return stmt.executeUpdate();
    }

}
