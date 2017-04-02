package tasz.mateusz.Canva;

import tasz.mateusz.Exception.FinishApplicationException;
import tasz.mateusz.Text;

/**
 * Created by Mateusz on 2017-03-31.
 */
public abstract class Canva {

    abstract public void showMenu();
    abstract public Canva perform () throws FinishApplicationException;


    public boolean validate(String command, String text) {
        command = command.toLowerCase();
        text = text.toLowerCase();

        if (Text.similarity(command, text) > 0.7) {
            if (command.equals(text) ||
                    (!command.equals(text) && Text.makeSure(command, text)))
                return true;
        }
        return false;
    }
}
