package tasz.mateusz.Canva;

/**
 * Created by Mateusz on 01.04.2017.
 */

import tasz.mateusz.Exception.FinishApplicationException;

public class ExitWindow extends Canva {
    ExitWindow(){
        showMenu();
    }

    @Override
    public void showMenu() {

        System.out.println();
        System.out.println("============================");
        System.out.println("|         GOOD BYE         |");
        System.out.println("============================");
        System.out.println("|                          |");
        System.out.println("| It was a pleasure to     |");
        System.out.println("| to work with You.        |");
        System.out.println("|==========================|");
    }

    @Override
    public Canva perform() throws FinishApplicationException {
        throw new FinishApplicationException("End this application");
    }



}


