package tasz.mateusz.Canva;

import tasz.mateusz.Exception.FinishApplicationException;
import tasz.mateusz.TextManipulation.Text;


/**
 * A class implements idea of abstract notion of window.
 * In case of version 1.0 the application support only
 * command line interface. Even though the presented view to
 * user is in a sort of window. This class does this magic.
 */
public abstract class AbstractWindow {

    /**
     * Virtual method which show menu. Should be overridden,
     * because each view (window) should have its own
     * notion of menu depending on serving functionality.
     */
    abstract public void showMenu();


    /**
     * Virtual method which perform action from user.
     * It reads input buffer and run proper method.
     *
     * @return window A type of window
     * @throws FinishApplicationException
     */
    abstract public AbstractWindow perform () throws FinishApplicationException;


    /**
     * Method check if similarity between two texts is high enough
     *
     * @param command a desired text looking for
     * @param text entry from user keyboar
     * @return bool Returns true if cammand and text are similar.
     */
    boolean validate(String command, String text) {
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
