package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;

/**
 * CouncilRegion is a specific Region of the Game in which can be placed a FamilyMember.
 * In the CouncilRegion there are informations about:
 * <ul>
 *  <li> {@link #regionID}: Region id of this Region.
 *  <li> {@link #councilSpace}: ActionSpace specific of this Region.
 *  <li> {@link CouncilRegion#occupants}: list of FamilyMember on this Region.
 * </ul>
 * <p>
 * In this Region the {@link #councilSpace} is a multiple ActionSpace and the list of 
 * {@link CouncilRegion#occupants} is needed to get the turn order of the players.
 * @author VaporUser.
 * @see CouncilRegion, Region, ResourceSet.
 */

public class CouncilRegion implements Region {

	private final int regionID;
	private ActionSpace councilSpace;
	private ArrayList<FamilyMember> occupants;
	
	/**
	 * Constructor of CouncilRegion. 
	 * <p>
	 * Initializes the {@link #councilSpace} as a {@link ActionSpace} with {@link ActionSpace#single} == false.
	 * and a new {@link ArrayList} of {@link #FamilyMember}.
	 *
	 * @author VaporUser.
	 * @param regionID.
	 * @see CouncilRegion, Region, ActionSpace, FamilyMember.
	 */
	public CouncilRegion(int regionID){
		this.regionID = regionID;
		this.councilSpace = new ActionSpace(null, 1, false, this.getRegionID(), 0);
		this.occupants = new ArrayList<FamilyMember>();
	}
	/**
	 * Add a {@link #familyMember} to the {@link CouncilRegion#occupants} of this {@link CouncilRegion}.
	 * <p>
	 * It uses the {@link ArrayList#add(Object)} method and {@link #familyMember} has to be not null.
	 * 
	 * @author VaporUser.
	 * @param familyMember.
	 * @see CouncilRegion, ArrayList, FamilyMember.
	 */
	public void putFamilyMember(FamilyMember familyMember){
		occupants.add(familyMember);
	}
	/**
	 * get method for the {@link CouncilRegion#occupants} of this {@link CouncilRegion}.
	 * <p>
	 * {@link CouncilRegion#occupants} is an ArrayList of {@link FamilyMember}.
	 * 
	 * @author VaporUser.
	 * @return this {@link CouncilRegion#occupants}.
	 * @see CouncilRegion, ArrayList, FamilyMember.
	 */
	public ArrayList<FamilyMember> getOccupants(){
		return this.occupants;
	}
	
	@Override
	public int getRegionID(){
		return this.regionID;
	}
	
	@Override
	public String toString(){ 			
		return this.councilSpace.toString();
	}
	@Override
	public ActionSpace getActionSpace(int id){
		try{
			if(councilSpace.getActionSpaceID() == id){
				return councilSpace;
			} return null;
		} catch(NullPointerException e){
			return null;
		}
	}
}
