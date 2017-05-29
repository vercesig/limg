package it.polimi.ingsw.GC_32.Server.Game.Board;

/**
 * MarketRegion is a specific Region of the {@link Game} in which can be placed a FamilyMember and performed a MarketAction.
 * In the MarketRegion there are informations about:
 * <ul>
 *  <li> {@link #regionID}: Region id of this Region.
 *  <li> {@link #track}: Array of specific ActionSpace of this Region.
 * </ul>
 * <p>
 * In this Region the {@link #track} is a list of {@link ActionSpace}. Every actionSpace of the track 
 * has a specific {@link ActionSpace#bonus} and it is a single actionSpace.
 *
 * @author VaporUser.
 * @see MarketRegion, Region, ActionSpace.
 */
public class MarketRegion implements Region  {
	
	private final int regionID;
	private ActionSpace[] track;
	
	/**
	 * Constructor of MarketRegion. 
	 * <p>
	 * Initializes the {@link #track} as a static array of {@link ActionSpace}.
	 * All the ActionSpace have {@link ActionSpace#single} == true and a different 
	 *  {@link ActionSpace#bonus}.
	 *
	 * @author VaporUser.
	 * @param regionID.
	 * @see MarketRegion, Region, ActionSpace.
	 */
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
	@Override
	public int getRegionID(){
		return this.regionID;
	}
	
	@Override
	public String toString(){		
		StringBuilder stringBuilder = new StringBuilder();
		for (ActionSpace actionSpace: track){
			stringBuilder.append(actionSpace.toString());
		}
		return new String (stringBuilder);
	}

	@Override
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
