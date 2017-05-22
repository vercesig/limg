package it.polimi.ingsw.GC_32.Server.Game.Board;


public class HarvestRegion extends Region{

	private ActionSpace[] track;
	//private ActionSpace singleActionSpace;
	//private ActionSpace multipleActionSpace;
	
	public HarvestRegion(int id){
		super(id);
		this.track = new ActionSpace[2]; // NUOVO ATTRIBUTO
		track[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		track[1] = new ActionSpace(null, 1, false, this.getRegionID(), 0);
		
	//	this.singleActionSpace = ActionSpace.create(null, 0, true, regionID, 0);
		//this.multipleActionSpace = ActionSpace.create(null, 0, false, regionID, 0);
	}
	
	/*public ActionSpace getSingleActionSpace(){		
		return this.singleActionSpace;
	}
	
	public ActionSpace getMultipleActionSpace(){
		return this.multipleActionSpace;
	}*/
	
	@Override
	public void print(){			//NUOVO METODO
		for (ActionSpace a: track){
			a.print();
		}
	}
	
	@Override
	public boolean contains(int id){			//NUOVO METODO
		for (ActionSpace action: this.track){
			if (action.getActionSpaceID() == id)
				return true;
		}	
		return false;
	}
	
	public void activateEffect(){
		
	}	
}
