package it.polimi.ingsw.GC_32.Common.Utils;

/**
 * contains some utilities which can be used anywhere in the code
 *
 */

public class Utils{
    private Utils(){}
    
    /**
     * sleep a thred safely, avoiding to wrap the sleep method into a try catch block every time the thread must be sleeped
     * @param time the time during which the Thread must sleep
     */
    public static void safeSleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}