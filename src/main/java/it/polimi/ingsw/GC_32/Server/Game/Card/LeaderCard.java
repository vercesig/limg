package it.polimi.ingsw.GC_32.Server.Game.Card;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class LeaderCard extends Card{

	private JsonObject requirements;
	private Effect flagEffect;
	private boolean onTheGame;
	private boolean abilityToken;
	
	public LeaderCard(String name, JsonObject requirements){
		super(name);
		this.onTheGame = false;
		this.abilityToken = false;
		this.requirements = requirements;
	}
	
	public Effect getFlagEffect(){
		try{
		return this.flagEffect;
		}catch(NullPointerException e){
			return null;
		}
	}
	
	public void registerFlagEffect(Effect e){
		this.flagEffect = e;
	}
	
	public JsonObject getRequirements(){
		return this.requirements;
	}
	
	public boolean hasATokenAbility(){
		return this.abilityToken;
	}
	
	public void turnCard(){
		abilityToken = false;
	}
	
	public void playCard(){
		this.onTheGame = true;
		this.abilityToken = true;
	}
	
	public boolean isOnTheGame(){
		return this.onTheGame;
	}
	
	public String toString(){
		return this.getName();
	}
}
