package tasz.mateusz.Canva;

/**
 * Created by taszzmat on 2017-03-31.
 */
import java.io.*;
import java.util.Scanner;

import tasz.mateusz.Canva.Canva;
import tasz.mateusz.Canva.EntryWindow;
import tasz.mateusz.Canva.MainWindow;
import tasz.mateusz.Action;
import tasz.mateusz.Text;

public class EntryWindow extends Canva {

    public EntryWindow(){

    }

    @Override
    public void showMenu() {

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
        System.out.println("Enter Your choice: ");

    }



}
