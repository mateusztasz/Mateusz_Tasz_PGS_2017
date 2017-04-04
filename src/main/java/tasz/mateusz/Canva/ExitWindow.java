package tasz.mateusz.Canva;

/**
 * Created by Mateusz on 01.04.2017.
 */

import tasz.mateusz.TextManipulation.Color;
import tasz.mateusz.Exception.FinishApplicationException;

public class ExitWindow extends AbstractWindow {
    ExitWindow(){
        showMenu();
    }

    /**
     * Method shows menu for ExitWindow (exit application)
     */
    @Override
    public void showMenu() {

        System.out.println(Color.CYAN);
        System.out.println("============================");
        System.out.println("|         GOOD BYE         |");
        System.out.println("============================");
        System.out.println("|                          |");
        System.out.println("| It was a pleasure        |");
        System.out.println("| to work with You.        |");
        System.out.println("|==========================|");
        System.out.print(Color.RESET);
    }


    /**
     * Method performs action for ExitWindow (exit application)
     */
    @Override
    public AbstractWindow perform() throws FinishApplicationException {
        throw new FinishApplicationException("End this application");
    }

}


