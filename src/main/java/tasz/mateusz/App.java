package tasz.mateusz;

import tasz.mateusz.Canva.AbstractWindow;
import tasz.mateusz.Canva.EntryWindow;
import tasz.mateusz.DataBase.DataBaseHandler;
import tasz.mateusz.Exception.FinishApplicationException;

import java.lang.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;


/**
 * Main application class. Implements application which is desired to use of Video Rental
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
     * An entry to the application. Main function executed
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

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
            String sqlDelete = "DELETE FROM RENTAL WHERE DueRented=?;";
            String sqlRentalStar = "select * from RENTAL WHERE DueRented=?;";
            String sqlUpdateMovieStack = "update MOVIE set Stack = Stack + 1 where MovieId = ?;";

            ResultSet resultSetRentaStar;

            Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.DATE, 2); // Test delete (simulate time in 2 days in future)
            java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

            // Get all rows from Rented movies
            resultSetRentaStar = db.executeQuery(sqlRentalStar, sqlDate.toString());

            //delete these for which date is today
            db.executeUpdate(sqlDelete, sqlDate.toString());

            //increase stack of returned videos
            while (resultSetRentaStar.next())
                db.executeUpdate(sqlUpdateMovieStack, resultSetRentaStar.getString("MovieId"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}

