package tasz.mateusz;
import java.sql.SQLException;
import java.sql.*;

import java.sql.*;

/**
 * Created by taszzmat on 2017-03-31.
 */
public class DataBase {

    private static final String CUSTOMER_LOGIN_SQL =
            "SELECT * FROM customer WHERE login = ? and pass = ?;";
    private PreparedStatement customerLoginStatement;

    public  String fun(){
        return "1";
    }

    public void connect() {
        Connection conn = null;
        Statement stmt = null;
        try {
            //Class.forName("org.xerial:sqlite");
            String url = "jdbc:sqlite:src/main/resources/db.sqlite";
            //url = "jdbc:sqlite:classes/db.sqlite";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");


            customerLoginStatement = conn.prepareStatement(CUSTOMER_LOGIN_SQL);
            customerLoginStatement.clearParameters();
            customerLoginStatement.setString(1,"Mateusz");
            customerLoginStatement.setString(2,"Tasz");

            ResultSet cid_set = customerLoginStatement.executeQuery();
            System.out.println(cid_set.getString("Address"));
            if (cid_set.next())
            {

            }








            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CUSTOMER;");
            System.out.println(rs.getString("Name"));

        }//catch (ClassNotFoundException e){}
        catch ( SQLException e) {
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
}
