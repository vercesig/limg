package it.polimi.ingsw.GC_32.Server.Game.Board;

/**
 * represent the HarvestRegion of the board into which can be placed a FamilyMember and performed an HarvestAction.
 * 
 * @see Region
 *
 */
public class HarvestRegion extends Region {

	/**
	 * initialize this region with the given regionID, setting up the action spaces inside this region
	 * @param regionID the ID of this region
	 */
	public HarvestRegion(int regionID){
		super(regionID,2);
		super.getTrack()[0] = new ActionSpace(null, 1, true, this.getRegionID(), 0);
		super.getTrack()[1] = new ActionSpace(null, 4, false, this.getRegionID(), 1);
	}
	
	public void activateEffect(){
		
	}
}
