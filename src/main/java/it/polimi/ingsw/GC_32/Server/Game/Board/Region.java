package it.polimi.ingsw.GC_32.Server.Game.Board;

public interface Region { // NUOVA CLASSE
	
	public int getRegionID();
	public String toString();
	public ActionSpace getActionSpace(int id);
}