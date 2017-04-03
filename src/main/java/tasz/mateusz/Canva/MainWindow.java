package tasz.mateusz.Canva;

import tasz.mateusz.TextManipulation.Color;
import tasz.mateusz.DataBaseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.text.*;

/**
 * Created by Mateusz on 2017-03-31.
 */
public class MainWindow extends AbstractWindow {
    /**
     * Database handler for this specific window.
     */
    private DataBaseHandler db;

    /**
     * Field for user identification. Help to work with logged in users.
     */
    private int userId;

    /**
     * Constructor
     *
     * @param db Pass database handler.
     */
    public MainWindow(DataBaseHandler db) {
        this(db, -1);
    }

    /**
     * Constructor
     *
     * @param db
     * @param userId
     */
    public MainWindow(DataBaseHandler db, int userId) {
        this.db = db;
        this.userId = userId;
        showMenu();
    }


    /**
     * Method shows menu for MainWindow (logged in user)
     */
    @Override
    public void showMenu() {

        System.out.println(Color.CYAN);
        System.out.println("============================");
        System.out.println("|         LOGGED IN        |");
        System.out.println("============================");
        System.out.println("|                          |");
        System.out.println("|       $> list            |");
        System.out.println("|       $> list <movie>    |");
        System.out.println("|       $> rent            |");
        System.out.println("|       $> rent <movie>    |");
        System.out.println("|       $> history         |");
        System.out.println("|       $> menu            |");
        System.out.println("|       $> help            |");
        System.out.println("|       $> logout          |");
        System.out.println("|       $> exit            |");
        System.out.println("============================");
        System.out.print(Color.RESET);

    }


    /**
     * Method performs action for MainWindow (logged in user)
     */
    @Override
    public AbstractWindow perform() {

        StringTokenizer commandToken;
        String command, response = null;
        System.out.println("Enter Your choice: ");
        System.out.print("$>");

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        try {
            response = r.readLine();
            commandToken = new StringTokenizer(response);
            command = commandToken.nextToken().trim();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return this;
        } catch (NoSuchElementException e) {
            // Empty buffer (i.e when enter hit)
            // - do not print message
            // - it would mess output
            // System.out.println(e.getMessage());
            return this;
        }

        if (validate(command, "list")) {
            // Check whether input is 'list' or 'list ...'
            if (commandToken.hasMoreElements()) showSpecificMovie(commandToken);
            else showList();

            return this;
        } else if (validate(command, "rent")) {
            // Check whether input is 'rent' or 'rent ...'
            if (commandToken.hasMoreElements()) rentSpecificMovie(commandToken);
            else showRentedList();

            return this;

        } else if (validate(command, "history")) {
            showHistory();
        } else if (validate(command, "menu")) {
            showMenu();
        } else if (validate(command, "help")) {
            showHelp();
        } else if (validate(command, "logout")) {
            userId = -1;
            return new EntryWindow(this.db);
        } else if (validate(command, "exit")) {
            return new ExitWindow();
        }
        return this;
    }


    /**
     * Method prints all rented movies by actual logged in user.
     */
    private void showRentedList() {
        try {
            PreparedStatement stmt = db.conn.prepareStatement("SELECT * FROM RENTAL WHERE CustomerId=?;");
            stmt.clearParameters();
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            System.out.println(Color.YELLOW);
            System.out.println("List of all rented videos:");
            System.out.print(Color.RESET);

            while (resultSet.next()) {

                stmt = db.conn.prepareStatement("SELECT Title FROM MOVIE WHERE MovieId=?;");
                stmt.clearParameters();
                stmt.setInt(1, resultSet.getInt("MovieId"));

                ResultSet resultSetSecond = stmt.executeQuery();
                if (resultSetSecond.next()) {

                    System.out.print("Title: ,,");
                    System.out.print(resultSetSecond.getString("Title"));
                    System.out.print("''  Rented till: ");
                    System.out.print(resultSet.getString("DueRented"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method rent movie specified by the string followed by rent ...
     */
    private void rentSpecificMovie(StringTokenizer commandToken) {

        // TODO update stock

        String title = "";
        int movieId;

        // Combine all rest command together (needed in case of two part title like ,,Toy Story'')
        while (commandToken.hasMoreTokens()) title += commandToken.nextToken() + " ";
        title = title.trim();

        try {

            PreparedStatement stmt = db.conn.prepareStatement("SELECT MovieId FROM MOVIE WHERE Title = ?;");
            PreparedStatement stmtHistory;
            stmt.clearParameters();
            stmt.setString(1, title);
            ResultSet resultSet = stmt.executeQuery();


            movieId = resultSet.getInt("MovieId");

            stmt = db.conn.prepareStatement("INSERT INTO RENTAL(CustomerId, MovieId, DueRented) VALUES(?,?,?);");
            stmtHistory = db.conn.prepareStatement("INSERT INTO RENTAL_HISTORY(CustomerId, MovieId, DueRented) VALUES(?,?,?);");

            stmt.clearParameters();
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);

            stmtHistory.clearParameters();
            stmtHistory.setInt(1, userId);
            stmtHistory.setInt(2, movieId);


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Rent for two days
            cal.add(Calendar.DATE, 2);

            java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

            stmt.setString(3, sqlDate.toString());
            stmtHistory.setString(3, sqlDate.toString());


            // Check if user is holding a video
            PreparedStatement stmt2 = db.conn.prepareStatement("SELECT * FROM RENTAL WHERE CustomerId=? AND MovieId=?;");
            stmt2.clearParameters();
            stmt2.setInt(1, userId);
            stmt2.setInt(2, movieId);
            ResultSet resultSet2 = stmt2.executeQuery();
            if (resultSet2.next() == false) {

                // Add row into database -  Rental table
                stmt.executeUpdate();
                System.out.print(Color.GREEN);
                System.out.println("You have succesfully rented a movie ,," + title + "''");
                System.out.println(Color.RESET);

                // Add row into database -  Rental_History table
                stmtHistory.executeUpdate();
            } else {
                System.out.print(Color.RED);
                System.out.println("Sorry. You are currently renting this video.");
                System.out.print(Color.RESET);
            }

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
        }
    }


    /**
     * Method shows history of every rental to logged in customer(user)
     */
    private void showHistory() {
        try {
            PreparedStatement stmt = db.conn.prepareStatement("SELECT * FROM RENTAL_HISTORY WHERE CustomerId=?;");
            stmt.clearParameters();
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            System.out.println(Color.YELLOW);
            System.out.println("List of all ever rented videos:");
            System.out.print(Color.RESET);

            while (resultSet.next()) {

                stmt = db.conn.prepareStatement("SELECT Title FROM MOVIE WHERE MovieId=?;");
                stmt.clearParameters();
                stmt.setInt(1, resultSet.getInt("MovieId"));

                ResultSet resultSetSecond = stmt.executeQuery();
                if (resultSetSecond.next()) {

                    System.out.print("Title: ,,");
                    System.out.print(resultSetSecond.getString("Title"));
                    System.out.print("''  Rented till: ");
                    System.out.print(resultSet.getString("DueRented"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method shows all availabele(number in stock in bigger than 0) movies from database
     */
    private void showList() {
        try {
            Statement stmt = db.conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM MOVIE WHERE Stack>0;");

            System.out.println(Color.YELLOW);
            System.out.println("List of all available videos:");
            System.out.print(Color.RESET);

            while (resultSet.next()) {

                System.out.print("Title: ,,");
                System.out.print(resultSet.getString("Title"));
                System.out.print("''  Rating: ");
                System.out.println(resultSet.getString("Rating"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * A method prints detail information about specific movie given as parameter
     *
     * @param commandToken
     */
    private void showSpecificMovie(StringTokenizer commandToken) {
        String command = "";

        // Combine all rest command together (needed in case of two part title like ,,Toy Story'')
        while (commandToken.hasMoreTokens()) command += commandToken.nextToken() + " ";
        command = command.trim();

        try {
            PreparedStatement stmt = db.conn.prepareStatement("SELECT * FROM MOVIE WHERE Title = ? ;");
            stmt.clearParameters();
            stmt.setString(1, command);
            ResultSet resultSet = stmt.executeQuery();

            System.out.println(Color.YELLOW);
            System.out.println("Information about movie:");
            System.out.print(Color.RESET);

            while (resultSet.next()) {

                System.out.print("Title: ,,");
                System.out.print(resultSet.getString("Title"));
                System.out.print("''  Rating: ");
                System.out.print(resultSet.getString("Rating"));
                System.out.print("  Stack: ");
                System.out.println(resultSet.getInt("Stack"));
                System.out.println();

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
        System.out.println("  -list             List all available movies in stock.");
        System.out.println("  -list <movie>     List information about <movie> e.g list Cars will");
        System.out.println("                    will list informations about ,,Cars'' movie.");
        System.out.println("  -rent             Show all rented movies.");
        System.out.println("  -rent <movie>     Rent a <movie> e.g rent Cars will");
        System.out.println("                    will rent ,,Cars'' movie.");
        System.out.println("  -history          Show your history of rented videos.");
        System.out.println("  -menu             Display a menu.");
        System.out.println("  -help             Print this help information.");
        System.out.println("  -exit             Close application.");
        System.out.print(Color.RESET);

    }

}
