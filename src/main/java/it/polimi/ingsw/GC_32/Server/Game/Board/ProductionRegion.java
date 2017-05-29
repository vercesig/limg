package it.polimi.ingsw.GC_32.Server.Game.Board;

/**
 * ProductionRegion is a specific Region of the {@link Game} in which can be placed a FamilyMember and performed a ProductionAction.
 * In the ProductionRegion there are informations about:
 * <ul>
 *  <li> {@link #regionID}: Region id of this Region.
 *  <li> {@link #track}: Array of specific ActionSpace of this Region.
 * </ul>
 * <p>
 * In this Region the {@link #track} is a list of {@link ActionSpace} in which the second element of {@link #track} is a
 * multiple ActionSpace.
 *
 * @author VaporUser
 * @see ProductionRegion, Region, ActionSpace.
 */
public class ProductionRegion implements Region {

	private ActionSpace[] track;
	private int regionID;	
	
	/**
	 * Constructor of ProductionRegion. 
	 * <p>
	 * Initializes the {@link #track} as a static array of {@link ActionSpace}.
	 * The second ActionSpace has {@link ActionSpace#single} == false.
	 *
	 * @author VaporUser.
	 * @param regionID.
	 * @see ProductionRegion, Region, ActionSpace.
	 */
	public ProductionRegion(int id){ 
	
		this.regionID = id;
		this.track = new ActionSpace[2];	//NUOVO ATTRIBUTO
		track[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		track[1] = new ActionSpace(null, 1, false, this.getRegionID(), 1);
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
