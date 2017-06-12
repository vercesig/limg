package it.polimi.ingsw.GC_32.Server.Game.Board;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class PersonalBonusTile {
	
	private ResourceSet personalBonus; 
	private boolean flagGameRule; // flag for the type of game rule: STANDARD or ADVANCED 
	
	public PersonalBonusTile(JsonObject config, boolean flagGameRule){
		
		this.personalBonus = new ResourceSet(config);
		this.flagGameRule = flagGameRule;
	}
	
	public ResourceSet getPersonalBonus(){
		return this.personalBonus;
	} 
	
	public boolean isFlagGameRule(){
		return this.flagGameRule;
	}
}
	