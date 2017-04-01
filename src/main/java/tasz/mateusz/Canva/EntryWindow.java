package tasz.mateusz.Canva;

/**
 * Created by Mateusz on 2017-03-31.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import tasz.mateusz.DataBaseHandler;
import tasz.mateusz.Text;

public class EntryWindow extends Canva {
    private DataBaseHandler db;

    public EntryWindow(DataBaseHandler db) {
        this(db, null);
    }

    public EntryWindow(DataBaseHandler db, String name) {
        this.db = db;
        showMenu();
    }

    @Override
    public void showMenu() {

        System.out.println();
        System.out.println("============================");
        System.out.println("|         WELCOME          |");
        System.out.println("============================");
        System.out.println("|                          |");
        System.out.println("|       $> login           |");
        System.out.println("|       $> create user     |");
        System.out.println("|       $> list users      |");
        System.out.println("|       $> help            |");
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

        if (validate(command, "login")) {
            int userId = db.login();

            if (userId > 0) return new MainWindow(this.db, userId);
            else    return this;

        } else if (validate(command, "create user")) {
            db.createUser();
            return this;
        } else if (validate(command, "list users")) {
            this.db.listCustomers();
        } else if (validate(command, "help")) {
            showHelp();
        } else if (validate(command, "exit")) {
            return new ExitWindow();
        } else {
            System.out.println("Command not recognized");
        }
        return this;
    }

    private boolean validate(String command, String text) {
        if (Text.similarity(command, text) > 0.7) {
            if (command.equals(text) ||
                    (!command.equals(text) && Text.makeSure(command, text)))
                return true;
        }
        return false;
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
