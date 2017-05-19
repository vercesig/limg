package it.polimi.ingsw.GC_32.Game.Board;


public class HarvestRegion {

	private ActionSpace singleActionSpace;
	private ActionSpace multipleActionSpace;
	
	public HarvestRegion(){
		this.singleActionSpace = ActionSpace.create(null, 0, true);
		this.multipleActionSpace = ActionSpace.create(null, 0, false);
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
