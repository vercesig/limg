package it.polimi.ingsw.GC_32.Common.Utils;

public class Tuple<T, U> {

    private final T firstArg;
    private final U secondArg;

    public Tuple(T arg1, U arg2) {
        this.firstArg = arg1;
        this.secondArg = arg2;
    }
    
    public T getFirstArg(){
        return firstArg;
    }
    
    public U getSecondArg(){
        return secondArg;
    }
}