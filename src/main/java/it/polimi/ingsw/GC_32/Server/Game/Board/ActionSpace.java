package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.FamilyMember;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;

public class ActionSpace{

	private ResourceSet bonus;
	private int actionValue;
	private ArrayList<FamilyMember> occupants;
	private boolean single;
	private final int regionID;
	private final int actionSpaceID;
	
	private ActionSpace(ResourceSet bonus, int actionValue, boolean single, int regionID, int actionSpaceID){
		this.bonus = bonus;
		this.actionValue = actionValue;
		this.occupants = new ArrayList<FamilyMember>();
		this.single = single;
		this.actionSpaceID = actionSpaceID;
		this.regionID = regionID;
	}
	
	public static ActionSpace create(ResourceSet bonus, int actionValue, boolean single){
		return new ActionSpace(bonus,actionValue,single);
	}
	
	public boolean isSingleActionSpace(){
		return this.single;
	}
	
	public ResourceSet getBonus(){
		return this.bonus;
	}
	
	public int getRegionID(){
		return this.regionID;
	}
	
	public int getActionSpaceID(){
		return this.actionSpaceID;
	}
	
	public int getActionValue(){
		return this.actionValue;
	}
	
	public ArrayList<Player> getPlayers(){
		ArrayList<Player> tmp = new ArrayList<Player>();
		for(FamilyMember familyMember : occupants){
			tmp.add(familyMember.getOwner());
		}
		return tmp;
	}
	
	public boolean addFamilyMember(FamilyMember familyMember){
		if(isBusy()){
			return false;
		}
		occupants.add(familyMember);
		return true;
	}
	
	public void removeFamilyMember(FamilyMember familyMember){
		this.occupants.remove(familyMember);
	}
	
	public boolean isBusy(){
		if(this.isSingleActionSpace()){
			return this.occupants.isEmpty();
		}
		return false;
	}
	
}
