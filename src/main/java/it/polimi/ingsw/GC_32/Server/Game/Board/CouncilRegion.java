package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;

public class CouncilRegion extends Region {

	private ArrayList<FamilyMember> occupants;
	
	public CouncilRegion(int regionID){
		super(regionID, 1);
		this.getTrack()[0] = new ActionSpace(null, 1, false, this.getRegionID(), 0);
		 
		this.occupants = new ArrayList<FamilyMember>();
	}
	
	public void putFamilyMember(FamilyMember familyMember){
		occupants.add(familyMember);
	}
	
	public ArrayList<FamilyMember> getOccupants(){
		return this.occupants;
	}
	
	public String toString(){ 			
		return this.getTrack()[0].toString();
	}
	public ActionSpace getActionSpace(int id){ 			
		return this.getTrack()[0];
	}
	
}
