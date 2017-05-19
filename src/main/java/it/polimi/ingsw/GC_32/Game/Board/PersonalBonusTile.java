package it.polimi.ingsw.GC_32.Game.Board;

import java.util.ArrayList;

public class PersonalBonusTile {
	
	private ArrayList<String> personalBonus; 
	private boolean flagGameRule; // flag for the type of game rule: STANDARD or ADVANCED 
	
	public PersonalBonusTile(ArrayList <String> personalBonus, boolean flagGameRule){
		
		this.personalBonus = personalBonus;
		this.flagGameRule = flagGameRule;
	}
	
	public ArrayList<String> getPersonalBonus(){
		return this.personalBonus;
	} 
	
	public boolean isFlagGameRule(){
		return this.flagGameRule;
	}
}
	