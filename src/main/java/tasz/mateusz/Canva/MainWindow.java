package tasz.mateusz.Canva;

import tasz.mateusz.DataBase.DataBaseHandler;
import tasz.mateusz.TextManipulation.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.sql.ResultSetMetaData;
import java.util.*;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class for create and manage with main window.
 * Main window is a window which is shown for logged in users.
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
     * @param db     database handler
     * @param userId user identfication number. if > 0 then logged in
     */
    MainWindow(DataBaseHandler db, int userId) {
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
        System.out.println("|       $> list <title>    |");
        System.out.println("|       $> rent            |");
        System.out.println("|       $> rent <title>    |");
        System.out.println("|       $> add             |");
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
        String command, response;
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

        } else if (validate(command, "add")) {
            addMovie();
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
            String sqlRental = "SELECT * FROM RENTAL WHERE CustomerId=?;";
            String sqlMovie = "SELECT Title FROM MOVIE WHERE MovieId=?;";

            ResultSet resultSetRental;
            ResultSet resultSetMovie;

            resultSetRental = db.executeQuery(sqlRental, userId);

            System.out.println(Color.YELLOW);
            System.out.println("List of all rented videos:");
            System.out.print(Color.RESET);

            while (resultSetRental.next()) {

                resultSetMovie = db.executeQuery(sqlMovie, resultSetRental.getString("MovieId"));
                if (resultSetMovie.next()) {

                    System.out.print("Title: ,,");
                    System.out.print(resultSetMovie.getString("Title"));
                    System.out.print("''  Rented till: ");
                    System.out.print(resultSetRental.getString("DueRented"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method add a new movie to databse.
     * It is allowed only for admin users.
     */
    private void addMovie() {

        // Allow this feature only for admins. Users with admin privileges are
        // Mateusz Tasz
        // PGS Software
        // For more see option list users in EntryWindow
        try {
            if (userId > 2) throw new Exception("This action is allowed only for admin. You are not.\n" +
                    "Please contact your administrator :)");
        } catch (Exception e) {
            System.out.println(Color.RED + e.getMessage() + Color.RESET);
            return;
        }

        String sqlAddMovie = "insert into MOVIE(Title, Rating, Duration, Stack) VALUES (?,?,?,?);";
        String sqlMovie = "select * from MOVIE;";
        String sqlMovieFind = "select * from MOVIE where Title = ?;";

        ResultSet resultSetMovie;
        ResultSetMetaData resultSetMovie_MetaData;

        Scanner scanLogin = new Scanner(System.in);
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            resultSetMovie = db.executeQuery(sqlMovie);
            resultSetMovie_MetaData = resultSetMovie.getMetaData();

            // Enter data to add into database
            for (int i = 2; i < resultSetMovie_MetaData.getColumnCount() + 1; i++) {

                System.out.print(resultSetMovie_MetaData.getColumnName(i) + " :");
                map.put(resultSetMovie_MetaData.getColumnName(i), scanLogin.nextLine());

            }

            ResultSet resultSet = db.executeQuery(sqlMovieFind, map.get("Title").toString());
            if (resultSet.next())
                throw new SQLException(Color.RED + "A movie with given title already exists." + Color.RESET);

            String title = map.get("Title").toString();
            float rating = Float.parseFloat(map.get("Rating").toString());
            int duration = Integer.parseInt(map.get("Duration").toString());
            int stack = Integer.parseInt(map.get("Stack").toString());

            if(title.equals(""))
                throw new SQLException("Title can not be empty.");
            if (rating > 10 || rating < 0)
                throw new SQLException("Rating value is not in range <0..10>.");
            if (duration < 0)
                throw new SQLException("Duration can not be negative.");
            if (stack < 0)
                throw new SQLException("Stack can not be negative.");

            db.executeUpdate(sqlAddMovie, title, rating, duration, stack);

            System.out.print(Color.GREEN);
            System.out.println("You have successfully added a new movie to database");
            System.out.println(Color.RESET);

        } catch (SQLException e) {
            System.out.println(Color.RED + "Error:" + e.getMessage() + Color.RESET);
        } catch (NumberFormatException e) {
            System.out.println(Color.RED + "Error:" + e.getMessage() + Color.RESET);
        }

    }


    /**
     * Method rent movie specified by the string followed by rent ...
     */
    private void rentSpecificMovie(StringTokenizer commandToken) {

        // TODO update stock

        String titleString = "";
        int movieId;

        // Combine all rest command together (needed in case of two part title like ,,Toy Story'')
        StringBuilder title = new StringBuilder("");
        while (commandToken.hasMoreTokens()) {
            title.append(commandToken.nextToken());
            title.append(" ");
        }
        titleString = title.toString().trim();

        try {
            String sql = "SELECT MovieId FROM MOVIE WHERE Title = ?;";
            String sqlRental = "INSERT INTO RENTAL(CustomerId, MovieId, DueRented) VALUES(?,?,?);";
            String sqlRentalHistory = "INSERT INTO RENTAL_HISTORY(CustomerId, MovieId, DueRented) VALUES(?,?,?);";
            String sqlCheckExistenceInRental = "SELECT * FROM RENTAL WHERE CustomerId=? AND MovieId=?;";
            String sqlUpdateMovieStack = "update MOVIE set Stack = Stack - 1 where MovieId = ?;";


            Calendar cal = Calendar.getInstance();
            // Set time for two day in future
            cal.add(Calendar.DATE, 2);
            java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

            // Find a movie by title
            ResultSet resultSet = db.executeQuery(sql, titleString);
            if (!resultSet.next())
                throw new SQLException("Sorry. There is no movie with this title.");

            else {
                // get id of movie
                movieId = resultSet.getInt("MovieId");

                // Check if user is holding a video
                ResultSet existRental = db.executeQuery(sqlCheckExistenceInRental, userId, movieId);

                if (!existRental.next()) {

                    // Add row into database -  Rental table
                    db.executeUpdate(sqlRental, userId, movieId, sqlDate.toString());

                    System.out.print(Color.GREEN);
                    System.out.println("You have succesfully rented a movie ,," + title + "''");
                    System.out.println(Color.RESET);

                    // Add row into database -  Rental_History table
                    db.executeUpdate(sqlRentalHistory, userId, movieId, sqlDate.toString());

                    // Decrase stack of this movie
                    db.executeUpdate(sqlUpdateMovieStack, movieId);
                } else
                    throw new SQLException("Sorry. You are currently renting this video.");

            }

        } catch (SQLException e) {
            System.out.println(Color.RED + e.getMessage() + Color.RESET);
        }
    }


    /**
     * Method shows history of every rental to logged in customer(user)
     */
    private void showHistory() {
        try {
            String sqlLookHistory = "SELECT * FROM RENTAL_HISTORY WHERE CustomerId=?;";
            String sqlGetTitle = "SELECT Title FROM MOVIE WHERE MovieId=?;";
            ResultSet resultSetHistory;
            ResultSet resultSetMovie;


            System.out.println(Color.YELLOW);
            System.out.println("List of all ever rented videos:");
            System.out.print(Color.RESET);

            resultSetHistory = db.executeQuery(sqlLookHistory, userId);

            // if found something in history
            while (resultSetHistory.next()) {

                // Look for every specification information is Movie table
                resultSetMovie = db.executeQuery(sqlGetTitle, resultSetHistory.getString("MovieId"));

                if (resultSetMovie.next()) {

                    System.out.print("Title: ,,");
                    System.out.print(resultSetMovie.getString("Title"));
                    System.out.print("''  Rented till: ");
                    System.out.print(resultSetHistory.getString("DueRented"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(Color.RED + e.getMessage() + Color.RESET);
        }
    }


    /**
     * Method shows all availabele(number in stock in bigger than 0) movies from database
     */
    private void showList() {
        try {
            String sql = "SELECT * FROM MOVIE WHERE Stack > ?;";
            ResultSet resultSet = db.executeQuery(sql, 0);

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
            System.out.println(Color.RED + e.getMessage() + Color.RESET);
        }
    }


    /**
     * A method prints detail information about specific movie given as parameter
     *
     * @param commandToken A whole wrap for strings in given paramet like [] (list [Toy Story])
     */
    private void showSpecificMovie(StringTokenizer commandToken) {
        String titleString = "";

        // Combine all rest command together (needed in case of two part title like ,,Toy Story'')
        StringBuilder title = new StringBuilder("");
        while (commandToken.hasMoreTokens()) {
            title.append(commandToken.nextToken());
            title.append(" ");
        }
        titleString = title.toString().trim();


        try {
            String sql = "SELECT * FROM MOVIE WHERE Title = ? ;";
            ResultSet resultSet = db.executeQuery(sql, titleString);

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
        System.out.println("  -list <title>     List information about <movie> e.g list Cars");
        System.out.println("                    will list informations about ,,Cars'' movie.");
        System.out.println("  -rent             Show all rented movies.");
        System.out.println("  -rent <title>     Rent a <movie> e.g rent Cars will");
        System.out.println("                    will rent ,,Cars'' movie.");
        System.out.println("  -add              Run a mechanism to add a new movie.");
        System.out.println("                    Option is available only for admin.");
        System.out.println("  -history          Show your history of rented videos.");
        System.out.println("  -menu             Display a menu.");
        System.out.println("  -help             Print this help information.");
        System.out.println("  -exit             Close application.");
        System.out.print(Color.RESET);

    }

}
