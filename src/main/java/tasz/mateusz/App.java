package tasz.mateusz;

/**
 * Created by taszzmat on 2017-03-31.
 */

import tasz.mateusz.Canva.Canva;
import tasz.mateusz.Canva.EntryWindow;
import tasz.mateusz.Canva.MainWindow;
import tasz.mateusz.Action;

import java.lang.*;
import java.util.Scanner;


public class App {

    private static DataBaseHandler db;


    /**
     * An entry to the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        db = new DataBaseHandler();
        db.connect();

        Canva window = new EntryWindow();
        String command;

        while (true) {

            window.showMenu();

            Scanner scan = new Scanner(System.in); //obiekt do odebrania danych od użytkownika

            command = scan.nextLine().trim();

            if (Text.similarity(command, "login") > 0.7) {
                if (command.equals("login") ||
                        (!command.equals("login") && Text.makeSure(command, "login"))) {
                    System.out.println("Lets log in");
                }

            } else if (Text.similarity(command, "create user") > 0.7) {
                if (command.equals("create user") ||
                        (!command.equals("create user") && Text.makeSure(command, "create user"))) {
                    System.out.println("Lets create a brand new user");
                }
            } else if (Text.similarity(command, "create user") > 0.7) {
                if (command.equals("create user") ||
                        (!command.equals("create user") && Text.makeSure(command, "create user"))) {
                    System.out.println("Lets create a brand new user");
                }
            } else if (Text.similarity(command, "list users") > 0.7) {
                if (command.equals("list users") ||
                        (!command.equals("list users") && Text.makeSure(command, "list users"))) {
                    System.out.println("Lets show a list");
                }
            } else if (Text.similarity(command, "help") > 0.7) {
                if (command.equals("help") ||
                        (!command.equals("help") && Text.makeSure(command, "help"))) {
                    System.out.println("Here a manual");
                }
            } else if (Text.similarity(command, "exit") > 0.7) {
                if (command.equals("exit") ||
                        (!command.equals("exit") && Text.makeSure(command, "exit"))) {
                    System.out.println("exit");
                }
            } else {
                System.out.println("Command not recognized");
            }


        }
    }
}
