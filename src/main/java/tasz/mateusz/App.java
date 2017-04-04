package tasz.mateusz;



import tasz.mateusz.Canva.AbstractWindow;
import tasz.mateusz.Canva.EntryWindow;
import tasz.mateusz.Exception.FinishApplicationException;

import java.lang.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;


/**
 * Created by Mateusz on 2017-03-31.
 *
 * @author Mateusz Tasz
 * @since 2017-03-31
 */
public class App implements Runnable {

    /**
     * A database handler
     */
    private static DataBaseHandler db;

    /**
     * A handler to window
     */
    private static AbstractWindow window;


    /**
     * An entry to the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO clean names of variables - specifically stmt in PrepareStatement

        db = new DataBaseHandler();
        window = new EntryWindow(db);

        // Start a thread to update a overdued rentals/
        (new Thread(new App())).start();


        while (true) {

            try {
                window = window.perform();
            } catch (FinishApplicationException e) {
                break;
            }
        }

        db.close();
    }

    /**
     * A function clears all row from RENTAL table for which due rented is reached.
     * This function is run once at the beginning of application.
     * <p>
     * // TODO It would be better to look for DueRented bigger than today() and compare by time not a string
     */
    public void run() {
        try {
            PreparedStatement stmt = db.conn.prepareStatement("DELETE FROM RENTAL WHERE DueRented=?;");
            stmt.clearParameters();

            Calendar cal = Calendar.getInstance();

            java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
            stmt.setString(1, sqlDate.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}

