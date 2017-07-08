package it.polimi.ingsw.GC_32.Server.Game.Board;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

/**
 * class representing the bonus tile of the player. bonus tiles are loaded from JSON external file, into which are detailed all the resoureces given by the activation
 * of the harvest or the production bonus
 *
 *<ul>
 *<li>{@link #personalHarvestBonus}: a ResourceSet representing the harvest bonus</li>
 *<li>{@link #personalProductionBonus}: a ResourceSet representing the production bonus</li>
 *<li>{@link #flagGameRule}: flag for the type of game rule: standard or advanced</li>
 *</ul>
 *
 *@see ResourceSet
 */
public class PersonalBonusTile {
	
	private ResourceSet personalProductionBonus;
	private ResourceSet personalHarvestBonus;
	private boolean flagGameRule;
	
	/**
	 * initilize this personal bonus tile looking to the JsonObjects passed as arguments which customize the personal bonus
	 * @param productionBonus JsonObject representing the ResourceSet of the bonus taken when a production action has been triggered
	 * @param harvestBonus JsonObject representing the ResourceSet of the bonus taken when an harvest action has been triggered
	 * @param flagGameRule the flag gor the type of game rule
	 */
	public PersonalBonusTile(JsonObject productionBonus, JsonObject harvestBonus, boolean flagGameRule){
		this.personalProductionBonus = new ResourceSet(productionBonus);
		this.personalHarvestBonus = new ResourceSet(harvestBonus);
		this.flagGameRule = flagGameRule;
	}
	
	/**
	 * allow to retrive the production bonus of this personal bonus tile
	 * @return a ResourceSet representing the production bonus of this bonus tile
	 */
	public ResourceSet getPersonalProductionBonus(){
		return this.personalProductionBonus;
	} 
	
	/**
	 * allow to retrive the harvest bonus of this personal bonus tile
	 * @return a ResourceSet representing the harvest bonus of this bonus tile
	 */
	public ResourceSet getPersonalHarvestBonus(){
		return this.personalHarvestBonus;
	}
	
	/**
	 * get the value of flagGameRule
	 * @return the value of flagGameRule, setted to false if game rule is setted to standard, true otherwise
	 */
	public boolean isFlagGameRule(){
		return this.flagGameRule;
	}
	
	/**
	 * return a string representation of the bonus tile
	 */
	public String toString(){
		return "production :"+personalProductionBonus.toString()+"\nharvest :"+personalHarvestBonus.toString();
	}
}
	