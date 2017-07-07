package it.polimi.ingsw.GC_32.Common.Utils;

public class Utils{
    private Utils(){}
    
    public static void safeSleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}