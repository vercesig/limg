package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;

/**
 * this class represent the council space
 * 
 * <ul>
 * <li>{@link #councilSpace}: the council region action space</li>
 * </ul>
 * 
 * @see ActionSpace, Region, FamilyMember
 */
public class CouncilRegion extends Region {

	private ActionSpace councilSpace;
	
	/**
	 * initialize the region
	 * @param regionID the ID of this region
	 */
	public CouncilRegion(int regionID){
		super(regionID, 1);
		this.councilSpace = new ActionSpace(null, 1, false, this.getRegionID(), 0);
		this.getTrack()[0] = this.councilSpace;
	}
	
	/**
	 * add a family member to the list of occupants of the councilSpace action space
	 * @param familyMember the family memeber to add
	 */
	public void putFamilyMember(FamilyMember familyMember){
		this.councilSpace.addFamilyMember(familyMember);
	}
	
	/**
	 * allows to retrive the list of occupants of this region, as a list of FamilyMember
	 * @return an ArrayList of FamilyMember
	 */
	public ArrayList<FamilyMember> getOccupants(){
		return this.councilSpace.getOccupants();
	}
	
	/**
	 * allows to retrive the councilSpace action space
	 * @return the council space
	 */
	public ActionSpace getCouncilSpace(){
		return this.councilSpace;
	}
	
	/**
	 * call the toString method of Region superclass
	 */
	public String toString(){ 			
		return this.councilSpace.toString();
	}
	
	/**
	 * call the flushActionSpace() method on the councilSpace action space
	 */
	public void flushRegion(){
		councilSpace.flushActionSpace();
	}
}
