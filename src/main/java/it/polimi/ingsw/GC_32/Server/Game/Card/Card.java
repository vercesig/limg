package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;
import java.util.HashSet;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

/**
 * class representing card. Each card is created by its JSON representation taken from the external JSON file.
 * 
 * <ul>
 * <li>{@link #instantEffect}: the list of instant effect of this card</li>
 * <li>{@link #permanentEffect}: the list of permanent effect of this card</li>
 * <li>{@link #name}: string representing the card's name</li>
 * <li>{@link #payloadInfo}: arrayList of JsonValue used to create some customize context (for example CHANGE effect context, which are strictly dependent by 
 * the card)</li>
 * <li>{@link #permanentEffectType}: HashSet of String containig all the type of insant epermanent of this card</li>
 * <li>{@link #instantEffectType}: HashSet of String containing all the type of instant effect of this card</li>
 * </ul>
 *
 * the last two attributes are used by the Game class to adapt the bheaviour of the action simulation on the specific card owned by the player
 * 
 *  @see Effect, Game
 *
 */
public class Card {

	private final String name;
	private ArrayList<Effect> instantEffect;
	private ArrayList<Effect> permanentEffect; 
	
	private ArrayList<JsonValue> payloadInfo; // need by effect type CHANGE
	
	private HashSet<String> permanentEffectType;
	private HashSet<String> instantEffectType;
	
	/**
	 * inizialize all the data structure of the object
	 * @param name the name of the card
	 */
	public Card(String name){
		this.name = name;
		this.instantEffect = new ArrayList<Effect>();
		this.permanentEffect = new ArrayList<Effect>();
		this.payloadInfo = new ArrayList<JsonValue>();
		this.permanentEffectType = new HashSet<String>();
		this.instantEffectType = new HashSet<String>();
	}
	
	/**
	 * allow to return the name of this card
	 * @return the name of the card
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * allow to add a JsonValue to the payloadInfo ArrayList
	 * @param payload the payload to add
	 */
	public void addPayload(JsonValue payload){
		this.payloadInfo.add(payload);
	}
	
	/**
	 * allow to retrives the list of payloads from payloadInfo arraylist
	 * @return the payload info list of this card
	 */
	public ArrayList<JsonValue> getPayloadInfo(){
		return this.payloadInfo;
	}
	
	/**
	 * allow to retrive the types of permanent effects which charachterize this card
	 * @return the set of permanent effect types of this card
	 */
	public HashSet<String> getPermanentEffectType(){
		return this.permanentEffectType;
	}
	
	/**
	 * allow to get all the instant effect of this card
	 * @return the list of instant effect of this card
	 * 
	 * @see Effect
	 */
	public ArrayList<Effect> getInstantEffect(){
		return this.instantEffect;
	}
	
	/**
	 * allow to get all the permanent effect of this card
	 * @return the list of permanent effect of this card
	 * 
	 * @see Effect
	 */
	public ArrayList<Effect> getPermanentEffect(){
		return this.permanentEffect;
	}
	
	/**
	 * allow to add the type of effect which has been registered into the effect list into the instantEffectType HashSet
	 * @param effectType the type of effect to register
	 */
	public void registerInstantEffectType(String effectType){
		this.instantEffectType.add(effectType);
	}
	
	/**
	 * allow to register an instant effect
	 * @param e the effect to add to the instantEffect list
	 */
	public void registerInstantEffect(Effect e){
		this.instantEffect.add(e);
	}
	
	/**
	 * allow to add the type of effect which has been registered into the effect list into the permanentEffectType HashSet
	 * @param effectType the type of effect to register
	 */
	public void registerPermanentEffectType(String effectType){
		this.permanentEffectType.add(effectType);
	}
	
	/**
	 * allow to register a permanent effect
	 * @param e the effect to add to the permanentEffect list
	 */
	public void registerPermanentEffect(Effect e){
		this.permanentEffect.add(e);
	}
}