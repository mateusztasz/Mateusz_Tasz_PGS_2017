package tasz.mateusz.Canva;

/**
 * Created by taszzmat on 2017-03-31.
 */
import java.io.*;

public class EntryWindow extends Canva {

    public  EntryWindow(){
        showMenu();
        performAction();
    }

    @Override
    public void showMenu() {

        System.out.println("============================");
        System.out.println("|         WELCOME          |");
        System.out.println("============================");
        System.out.println("| Options:                 |");
        System.out.println("|        1. Login          |");
        System.out.println("|        2. Create user    |");
        System.out.println("|        3. Help           |");
        System.out.println("|        4. Exit           |");
        System.out.println("============================");
        System.out.println("Enter Your choice: ");

    }

    public void performAction(){

    }

}
