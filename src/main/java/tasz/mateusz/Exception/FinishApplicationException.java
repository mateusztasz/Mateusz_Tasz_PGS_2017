package tasz.mateusz.Exception;

/**
 * Class of my own exception. Allows to exit application.
 */

public class FinishApplicationException extends Exception {

    /**
     * Constructor
     *
     * @param message Message passed
     */
    public FinishApplicationException(String message) {
        super(message);
    }


}