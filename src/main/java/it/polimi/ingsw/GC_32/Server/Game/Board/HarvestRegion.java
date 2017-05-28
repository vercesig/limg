package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;

public class HarvestRegion implements Region {

	private final int regionID;
	private ActionSpace[] track;
	
	public HarvestRegion(int regionID){
		this.regionID = regionID;
		this.track = new ActionSpace[2]; // NUOVO ATTRIBUTO
		track[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		track[1] = new ActionSpace(null, 1, false, this.getRegionID(), 1);
	}
	
	public void activateEffect(){
		
	}
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
