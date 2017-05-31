package it.polimi.ingsw.GC_32.Server.Game.Board;

/**
 * represents a set of ActionSpace in which the Player can place its FamilyMember.
 * <p>
 * 
 * @see ActionSpace, FamilyMember, Player
 * @author VaporUser
 */
public class Region { 
	
	private final int regionID;
	private ActionSpace[] track;
	
	public Region(int regionID, int numberOfActionSpaces){
		this.regionID = regionID;
		this.track = new ActionSpace[numberOfActionSpaces];
	}
	
	public Region(int regionID){
		this.regionID = regionID;
	}
	
	/**
	 * returns a String which can easily printed on the screen. It is helpful for debugging, testing
	 * and visualize all the attributes of the Region
	 * <p>
	 * This methods calls the toString() methods of ActionSpace class. If the ActionSpace is null, it 
	 * throws an NullPointerException. 
	 * 
	 * @return 		a String which has all the information of the Region and its ActionSpace.
	 * @see 		Region, TowerRegion, ProductionRegion, HarverstRegion, CouncilRegion, MarketRegion, ActionSpace
	 */
	public String toString(){		
		StringBuilder stringBuilder = new StringBuilder();
		for (ActionSpace actionSpace: track){
			stringBuilder.append(actionSpace.toString());
		}
		return new String (stringBuilder);
	}

	/**
	 * returns the ActionSpace of the Region which has its ActionSpaceID as id.
	 * <p>
	 * This methods search if in the Region is present an ActionSpace with the passed argument id.
	 * If it is not valid, returns null. Otherwise return the specific ActionSpace object. 
	 * 
	 * @param id	an id number of the ActionSpace to get from this Region.
	 * @return		an ActionSpace with the passed id.
	 * @see 		Region, TowerRegion, ProductionRegion, HarverstRegion, CouncilRegion, MarketRegion, ActionSpace
	 */
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
	
	/**
	 * returns this Region id. 
	 *  <p>
	 * All implementations of Region have a final attribute RegionID which is passed
	 * to the Class Constructor. 
	 * 
	 * @return 		this Region specific RegionID.
	 * @see 		Region, TowerRegion, ProductionRegion, HarverstRegion, CouncilRegion, MarketRegion
	 */
	public int getRegionID(){
		return this.regionID;
	}
	
	/**
	 * returns all the actionSpaces inside this specific region.
	 * 
	 * @return		this Region specific set of actionSpaces
	 */
	public ActionSpace[] getTrack(){
		return this.track;
	}
}