package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;

public class CouncilRegion extends Region {

	private ActionSpace councilSpace;
	
	public CouncilRegion(int regionID){
		super(regionID, 1);
		this.councilSpace = new ActionSpace(null, 1, false, this.getRegionID(), 0);
		this.getTrack()[0] = this.councilSpace;
	}
	
	public void putFamilyMember(FamilyMember familyMember){
		this.councilSpace.addFamilyMember(familyMember);
	}
	
	public ArrayList<FamilyMember> getOccupants(){
		return this.councilSpace.getOccupants();
	}
	
	public ActionSpace getCouncilSpace(){
		return this.councilSpace;
	}
	
	public String toString(){ 			
		return this.councilSpace.toString();
	}
	
	public void flushRegion(){
		councilSpace.flushActionSpace();
	}
}
