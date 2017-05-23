package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;

public class HarvestRegion {

	private ActionSpace singleActionSpace;
	private ActionSpace multipleActionSpace;
	private final int regionID;
	
	public HarvestRegion(int regionID){
		this.regionID = regionID;
		this.singleActionSpace = new ActionSpace(new ResourceSet(), 0, true, regionID, 0);
		this.multipleActionSpace = new ActionSpace(new ResourceSet(), 0, false, regionID, 0);
	}
	
	public ActionSpace getSingleActionSpace(){		
		return this.singleActionSpace;
	}
	
	public ActionSpace getMultipleActionSpace(){
		return this.multipleActionSpace;
	}
	
	public void activateEffect(){
		
	}
	
	
}
