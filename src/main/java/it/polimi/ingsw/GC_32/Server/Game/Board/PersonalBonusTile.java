package it.polimi.ingsw.GC_32.Server.Game.Board;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class PersonalBonusTile {
	
	private ResourceSet personalProductionBonus;
	private ResourceSet personalHarvestBonus;
	private boolean flagGameRule; // flag for the type of game rule: STANDARD or ADVANCED 
	
	public PersonalBonusTile(JsonObject productionBonus, JsonObject harvestBonus, boolean flagGameRule){
		this.personalProductionBonus = new ResourceSet(productionBonus);
		this.personalHarvestBonus = new ResourceSet(harvestBonus);
		this.flagGameRule = flagGameRule;
	}
	
	public ResourceSet getPersonalProductionBonus(){
		return this.personalProductionBonus;
	} 
	
	public ResourceSet getPersonalHarvestBonus(){
		return this.personalHarvestBonus;
	}
	
	public boolean isFlagGameRule(){
		return this.flagGameRule;
	}
	
	public String toString(){
		return "BONUS TILE\n"+"production :"+personalProductionBonus.toString()+"\nharvest :"+personalHarvestBonus.toString();
	}
}
	