package it.polimi.ingsw.GC_32.Server.Game.Board;

public class MarketRegion implements Region  {
	
	private final int regionID;
	private ActionSpace[] track;
	
	public MarketRegion(int regionID){
		this.regionID = regionID;
		this.track = new ActionSpace[4]; // NUOVO ATTRIBUTO
		track[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		track[1] = new ActionSpace(null, 1, true, this.getRegionID(), 1);
		track[2] = new ActionSpace(null, 1, true, this.getRegionID(), 2);
		track[3] = new ActionSpace(null, 1, true, this.getRegionID(), 3);
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
