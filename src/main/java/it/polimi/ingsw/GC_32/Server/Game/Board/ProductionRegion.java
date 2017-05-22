package it.polimi.ingsw.GC_32.Server.Game.Board;

public class ProductionRegion extends Region {

	private ActionSpace[] track;
	
	//private ActionSpace singleActionSpace;
	//private ActionSpace multipleActionSpace;
	
	
	public ProductionRegion(int id){ 
		super(id);
	
		this.track = new ActionSpace[2];	//NUOVO ATTRIBUTO
		track[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		track[1] = new ActionSpace(null, 1, false, this.getRegionID(), 0);
	}
	
	/*public ActionSpace getSingleActionSpace(){		
		return this.singleActionSpace;
	}
	
	public ActionSpace getMultipleActionSpace(){
		return this.multipleActionSpace;
	}*/
	
	@Override
	public boolean contains(int id){ 	//NUOVO METODO
		for (ActionSpace action: this.track){
			if (action.getActionSpaceID() == id)
				return true;
		}	
		return false;
	}
	
	@Override
	public void print(){		//NUOVO METODO
		for (ActionSpace a: track){
			a.print();
		}
	}
	
	public void activateEffect(){
		
	}
	
}
