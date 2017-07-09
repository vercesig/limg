package it.polimi.ingsw.GC_32.Common.Utils;

/**
 * interface implemented by threads which can be stopped calling the kill() method
 * 
 */
public interface KillableRunnable extends Runnable{
	void kill();
}