package tasz.mateusz.Canva;

import tasz.mateusz.DataBaseHandler;
import tasz.mateusz.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Created by Mateusz on 2017-03-31.
 */
public class MainWindow extends Canva {
    private DataBaseHandler db;
    private int userId;

    public MainWindow(DataBaseHandler db) {
        this(db, -1);
    }

    public MainWindow(DataBaseHandler db, int userId){
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
        System.out.println("|       $> rent            |");
        System.out.println("|       $> show history    |");
        System.out.println("|       $> help            |");
        System.out.println("|       $> log out         |");
        System.out.println("|       $> exit            |");
        System.out.println("============================");

    }

    @Override
    public Canva perform() {

        String command;
        System.out.println("Enter Your choice: ");
        System.out.print("$>");
        Scanner scan = new Scanner(System.in); //obiekt do odebrania danych od użytkownika

        command = scan.nextLine().trim();

        if (Text.similarity(command, "rent") > 0.7) {
            if (command.equals("rent") ||
                    (!command.equals("rent") && Text.makeSure(command, "rent"))) {
                System.out.println("Lets rent in");
                return new MainWindow(this.db);
            }

        } else if (Text.similarity(command, "show history") > 0.7) {
            if (command.equals("show history") ||
                    (!command.equals("show history") && Text.makeSure(command, "show history"))) {
                System.out.println("Lets show history");
            }
        } else if (Text.similarity(command, "log out") > 0.7) {
            if (command.equals("log out") ||
                    (!command.equals("log out") && Text.makeSure(command, "log out"))) {
                System.out.println("Here a logout");
                return new EntryWindow(this.db);
            }

        } else if (Text.similarity(command, "help") > 0.7) {
            if (command.equals("help") ||
                    (!command.equals("help") && Text.makeSure(command, "help"))) {
                showHelp();

            }
        } else if (Text.similarity(command, "exit") > 0.7) {
            if (command.equals("exit") ||
                    (!command.equals("exit") && Text.makeSure(command, "exit"))) {
                return new ExitWindow();
            }
        } else {
            System.out.println("Command not recognized");
        }
        return this;
    }


    public void showHelp() {

        System.out.println();
        System.out.println("=============HELP===========");
        System.out.println("Explanation of each option:");
        System.out.println("  -login            Log into database in order to work with application.");
        System.out.println("  -create user      Create a new user in case you are a new one.");
        System.out.println("  -list users       Check all users in database");
        System.out.println("  -help             Print this help information.");
        System.out.println("  -exit             Close application.");

    }
}
