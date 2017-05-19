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
	
	private ActionSpace(ResourceSet bonus, int actionValue, boolean single){
		super();
		this.bonus = bonus;
		this.actionValue = actionValue;
		this.occupants = new ArrayList<FamilyMember>();
		this.single = single;
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
	
	public ArrayList<Player> getPlayers(){
		ArrayList<Player> tmp = new ArrayList<Player>();
		for(FamilyMember familyMember : occupants){
			tmp.add(familyMember.getOwner());
		}
		return tmp;
	}
	
	public int getActionValue(){
		return this.actionValue;
	}
	
	public boolean addFamilyMember(FamilyMember familyMember){
		if(isBusy()){
			return false;
		}
		occupants.add(familyMember);
		return true;
	}
	
	public void removeFamilyMember(){
		
	}
	
	public boolean isBusy(){
		if(this.isSingleActionSpace()){
			return this.occupants.isEmpty();
		}
		return false;
	}
	
}
