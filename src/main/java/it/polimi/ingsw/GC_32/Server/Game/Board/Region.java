package it.polimi.ingsw.GC_32.Server.Game.Board;

public class Region { // NUOVA CLASSE
	
	private final int regionID;
	
	public Region(int regionID){
		this.regionID = regionID;
	}
	
	/*public void setRegionID(int regionID){ // NON SERVE
		this.regionID = regionID;
	}*/
	public int getRegionID(){ 
		return this.regionID;
	}
	
	public void print(){}

	public boolean contains(int id){
		return false;
	}
}