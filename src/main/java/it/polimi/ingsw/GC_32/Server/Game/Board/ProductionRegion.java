package it.polimi.ingsw.GC_32.Server.Game.Board;

public class ProductionRegion {

	private ActionSpace singleActionSpace;
	private ActionSpace multipleActionSpace;
	private final int regionID;
	
	public ProductionRegion(int regionID){
		this.regionID = regionID;
		this.singleActionSpace = ActionSpace.create(null, 0, true, regionID, 0);
		this.multipleActionSpace = ActionSpace.create(null, 0, false, regionID, 0);
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
