package it.polimi.ingsw.GC_32.Server.Game.Board;

public class ProductionRegion implements Region {

	private ActionSpace[] track;
	private int regionID;	
	
	public ProductionRegion(int id){ 
	
		this.regionID = id;
		this.track = new ActionSpace[2];	//NUOVO ATTRIBUTO
		track[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		track[1] = new ActionSpace(null, 1, false, this.getRegionID(), 0);
	}
	
	public void activateEffect(){
		
	}
	
	// implementazioni Metodi Astratti Region
	public int getRegionID(){
		return this.regionID;
	}
	
	public String toString(){		
		StringBuilder stringBuilder = new StringBuilder();
		for (ActionSpace actionSpace: track){
			stringBuilder.append(actionSpace.toString());
		}
		return new String (stringBuilder);
	}
	
	public ActionSpace getActionSpace(int id){
		try{
			for (ActionSpace actionSpace: track){
				if(actionSpace.getActionSpaceID() == id)
					return actionSpace;
			} return null;
		}catch(NullPointerException e){
			return null;
		}
	}
}
