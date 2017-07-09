package it.polimi.ingsw.GC_32.Common.Utils;

/**
 * data structure which represent a tuple of two elements
 *
 * @param <T> the first argument of the tuple
 * @param <U> the second argument of the tuple
 */
public class Tuple<T, U> {

    private final T firstArg;
    private final U secondArg;

    /**
     * create a new tuple with the arguments passed as arguments
     * @param arg1 the first argument ot the new tuple
     * @param arg2 the second argument of the new tuple
     */
    public Tuple(T arg1, U arg2) {
        this.firstArg = arg1;
        this.secondArg = arg2;
    }
    
    /**
     * allow to retrive the first tuple argument
     * @return the first argument of the tuple
     */
    public T getFirstArg(){
        return firstArg;
    }
    
    /**
     * allow to retrive the second tuple argument
     * @return the second argument of the tuple
     */
    public U getSecondArg(){
        return secondArg;
    }
}