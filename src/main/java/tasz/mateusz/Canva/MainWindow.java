package tasz.mateusz.Canva;

import tasz.mateusz.DataBaseHandler;
import tasz.mateusz.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.util.Date;
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
public class MainWindow extends Canva {
    private DataBaseHandler db;
    private int userId;

    public MainWindow(DataBaseHandler db) {
        this(db, -1);
    }

    public MainWindow(DataBaseHandler db, int userId) {
        this.db = db;
        this.userId = userId;
        showMenu();
    }

    @Override
    public void showMenu() {

        System.out.println();
        System.out.println("============================");
        System.out.println("|         LOGGED IN        |");
        System.out.println("============================");
        System.out.println("|                          |");
        System.out.println("|       $> list            |");
        System.out.println("|       $> list <movie>    |");
        System.out.println("|       $> rent            |");
        System.out.println("|       $> rent <movie>    |");
        System.out.println("|       $> show history    |");
        System.out.println("|       $> help            |");
        System.out.println("|       $> logout         |");
        System.out.println("|       $> exit            |");
        System.out.println("============================");

    }

    @Override
    public Canva perform() {

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
        }catch (NoSuchElementException e ){
            // Empty buffer (i.e when enter hit)
            // - do not print message
            // - it would mess output
            //System.out.println(e.getMessage());
            return this;
        }


        if (validate(command, "list")) {
            if(commandToken.hasMoreElements()) showSpecificMovie(commandToken);
            else showList() ;

            return this;
        }
        else if (validate(command, "rent")) {
            if(commandToken.hasMoreElements()) rentSpecificMovie(commandToken);
            else showRentedList() ;

            return this;


        } else if (validate(command, "show history")) {
            System.out.println("Lets show history");
        } else if (validate(command, "help")) {
            showHelp();
        } else if (validate(command, "logout")) {
            userId = -1;
            return new EntryWindow(this.db);
        } else if (validate(command, "exit")) {
            return new ExitWindow();
        } else {
            //System.out.println("Command not recognized");
        }
        return this;
    }

    private  void showRentedList(){
        try {
            PreparedStatement stmt = db.conn.prepareStatement("select * from RENTAL where CustomerId=?;");
            stmt.clearParameters();
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();


            System.out.println();
            System.out.println("List of all rented videos.");

            while (resultSet.next()) {


                stmt = db.conn.prepareStatement("select Title from MOVIE where MovieId=?;");
                stmt.clearParameters();
                stmt.setInt(1, resultSet.getInt("MovieId"));

                ResultSet resultSetSecond = stmt.executeQuery();
                if (resultSetSecond.next()) {

                    System.out.print("Title: ,,");
                    System.out.print(resultSetSecond.getString("Title"));
                    System.out.print("''  Rented till: ");

                    // TODO  Error parsing time stamp
                    System.out.print(resultSet.getString("DueRented"));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private  void rentSpecificMovie(StringTokenizer  commandToken){
        String title =  "";
        int movieId;

        // Combine all rest command together (needed in case of two part title like ,,Toy Story'')
        while (commandToken.hasMoreTokens()) title += commandToken.nextToken() + " ";
        title = title.trim();

        try {

            PreparedStatement stmt = db.conn.prepareStatement("select MovieId from MOVIE where Title = ?;");
            stmt.clearParameters();
            stmt.setString(1, title);
            ResultSet resultSet = stmt.executeQuery();

            movieId = resultSet.getInt("MovieId");

            stmt = db.conn.prepareStatement("insert into RENTAL(CustomerId, MovieId, DueRented) values(?,?,?);");
            stmt.clearParameters();
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Rent for one day
            cal.add(Calendar.DATE, 1);

            java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

            stmt.setString(3, sqlDate.toString());
            //stmt.setString(3, "12 05 2009");

            // Add row into database
            stmt.executeUpdate();
            System.out.println(sqlDate.toString());
            System.out.println("You have succesfully rented a movie ,,"+title+"''");
            System.out.println();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private void showList(){
        try {
            Statement stmt = db.conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from MOVIE where Stack>0;");

            System.out.println();
            System.out.println("List of all available videos.");

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

    private void showSpecificMovie(StringTokenizer  commandToken) {
        String command =  "";

        // Combine all rest command together (needed in case of two part title like ,,Toy Story'')
        while (commandToken.hasMoreTokens()) command += commandToken.nextToken() + " ";
        command = command.trim();

        try {
            PreparedStatement stmt = db.conn.prepareStatement("select * from MOVIE where Title = ? ;");
            stmt.clearParameters();
            stmt.setString(1, command);
            ResultSet resultSet = stmt.executeQuery();

            System.out.println();
            System.out.println("List of all available videos.");

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

    public void showHelp() {

        System.out.println();
        System.out.println("=============HELP===========");
        System.out.println("Explanation of each option:");
        System.out.println("  -rent             Rent a video");
        System.out.println("  -show history     Show your video rent history.");
        System.out.println("  -help             Print this help information.");
        System.out.println("  -exit             Close application.");

    }
}
