package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;

public class CouncilRegion {

	private final int regionID;
	private ArrayList<FamilyMember> occupants;
	
	public CouncilRegion(int regionID){
		this.regionID = regionID;
		this.occupants = new ArrayList<FamilyMember>();
	}
	
	public void putFamilyMember(FamilyMember familyMember){
		occupants.add(familyMember);
	}
	
	public ArrayList<FamilyMember> getOccupants(){
		return this.occupants;
	}
	
}
