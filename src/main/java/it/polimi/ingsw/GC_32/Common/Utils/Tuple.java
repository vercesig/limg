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

    @Override
    public boolean equals(Object obj) {      
       if (this == obj)
          return true;
       if (obj == null || getClass() != obj.getClass())
          return false;
            
       Tuple <T,U> other = (Tuple <T,U>) obj;
       if (other.getFirstArg() != null && other.getSecondArg()!= null) {
    	   return this.getFirstArg().equals(other.getFirstArg()) &&
       			this.getSecondArg().equals(other.getSecondArg());
       }
       else
       return false;
    }
}