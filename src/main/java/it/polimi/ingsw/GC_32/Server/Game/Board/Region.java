package it.polimi.ingsw.GC_32.Server.Game.Board;

/**
 * represents a set of ActionSpace in which the Player can place its FamilyMember.
 * 
 * <ul>
 * <li>{@link #regionID}: the ID of this region</li>
 * <li>{@link #track}: the array of action spaces which compose this region</li>
 * </ul>
 * 
 * @see ActionSpace, FamilyMember, Player
 * @author VaporUser
 */
public class Region { 
	
	private final int regionID;
	private ActionSpace[] track;
	
	/**
	 * initialize the Region with the given regionID and the given number of action spaces
	 * @param regionID the ID of this region 
	 * @param numberOfActionSpaces the number of action spaces which compose this region
	 */
	public Region(int regionID, int numberOfActionSpaces){
		this.regionID = regionID;
		this.track = new ActionSpace[numberOfActionSpaces];
	}
	
	/**
	 * initialize this region with the given regionID
	 * @param regionID the ID of this region
	 */
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
		StringBuilder stringBuilder = new StringBuilder("A");
		for (ActionSpace actionSpace: track){
			if(actionSpace != null){
				stringBuilder.append(actionSpace.toString());
			}
		}
		return stringBuilder.toString();
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
		for(ActionSpace actionSpace: track){
			if(actionSpace != null &&
			   actionSpace.getActionSpaceID() == id){
				return actionSpace;
			}
		}
		return null;
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
	
	/**
	 * flush this region, calling the flushActionSpace() method on all the action spaces which compose the region
	 */
	public void flushRegion(){
		for(int i=0; i<track.length; i++){
			track[i].flushActionSpace();
		}
	}
}
