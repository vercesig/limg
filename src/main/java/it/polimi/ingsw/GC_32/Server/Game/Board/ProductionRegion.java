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
public class ProductionRegion extends Region {

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
	public ProductionRegion(int regionID){ 
		super(regionID,2);
		super.getTrack()[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		super.getTrack()[1] = new ActionSpace(null, 4, false, this.getRegionID(), 1);
	}
	
	public void activateEffect(){
		
	}
}
