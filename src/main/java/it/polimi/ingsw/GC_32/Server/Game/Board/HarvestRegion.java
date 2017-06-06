package it.polimi.ingsw.GC_32.Server.Game.Board;

public class HarvestRegion extends Region {

	public HarvestRegion(int regionID){
		super(regionID,2);
		super.getTrack()[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		super.getTrack()[1] = new ActionSpace(null, 1, false, this.getRegionID(), 1);
	}
	
	public void activateEffect(){
		
	}	
}
