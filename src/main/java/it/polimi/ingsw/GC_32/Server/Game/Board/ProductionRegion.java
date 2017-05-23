package it.polimi.ingsw.GC_32.Server.Game.Board;

public class ProductionRegion {

	private ActionSpace singleActionSpace;
	private ActionSpace multipleActionSpace;
	
	public ProductionRegion(int regionID){
		this.singleActionSpace = new ActionSpace(null, 0, true, regionID, 0);
		this.multipleActionSpace = new ActionSpace(null, 0, false, regionID, 0);
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
