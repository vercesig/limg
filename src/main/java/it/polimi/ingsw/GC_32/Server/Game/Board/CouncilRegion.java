package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;

public class CouncilRegion implements Region {

	private final int regionID;
	private ActionSpace councilSpace;
	private ArrayList<FamilyMember> occupants;
	
	public CouncilRegion(int regionID){
		this.regionID = regionID;
		this.councilSpace = new ActionSpace(null, 1, false, this.getRegionID(), 0);
		this.occupants = new ArrayList<FamilyMember>();
	}
	
	public void putFamilyMember(FamilyMember familyMember){
		occupants.add(familyMember);
	}
	
	public ArrayList<FamilyMember> getOccupants(){
		return this.occupants;
	}
	public int getRegionID(){
		return this.regionID;
	}
	
	public String toString(){ 			
		return this.councilSpace.toString();
	}
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
