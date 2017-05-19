package it.polimi.ingsw.GC_32.Server.Game.Board;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class PersonalBonusTile {
	
	private Effect personalBonus; 
	private boolean flagGameRule; // flag for the type of game rule: STANDARD or ADVANCED 
	
	public PersonalBonusTile(Effect personalBonus, boolean flagGameRule){
		
		this.personalBonus = personalBonus;
		this.flagGameRule = flagGameRule;
	}
	
	public Effect getPersonalBonus(){
		return this.personalBonus;
	} 
	
	public boolean isFlagGameRule(){
		return this.flagGameRule;
	}
}
	