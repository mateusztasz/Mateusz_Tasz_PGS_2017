package tasz.mateusz;

/**
 * Created by taszzmat on 2017-03-31.
 */

import tasz.mateusz.Canva.Canva;
import tasz.mateusz.Canva.EntryWindow;

import java.lang.*;


public class App {

    private static DataBase db;


    /**
     * An entry to the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        db = new DataBase();
        db.connect();



        Canva c = new EntryWindow();



    }
}
