package tasz.mateusz.Canva;

import tasz.mateusz.Exception.FinishApplicationException;

/**
 * Created by Mateusz on 2017-03-31.
 */
public abstract class Canva {

    abstract public void showMenu();
    abstract public Canva perform () throws FinishApplicationException;

}
