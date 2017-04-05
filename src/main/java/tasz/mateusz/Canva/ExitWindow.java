package tasz.mateusz.Canva;

import tasz.mateusz.TextManipulation.Color;
import tasz.mateusz.Exception.FinishApplicationException;


/**
 * Class for create and manage with exit window.
 * Exit window is a window which is shown to say good bye to user.
 */
public class ExitWindow extends AbstractWindow {
    ExitWindow() {
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
     *
     * @throws FinishApplicationException Exception which cause end of application
     */
    @Override
    public AbstractWindow perform() throws FinishApplicationException {
        throw new FinishApplicationException("End this application");
    }

}


