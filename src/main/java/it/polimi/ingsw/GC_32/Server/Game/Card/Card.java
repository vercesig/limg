package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;
import java.util.HashSet;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class Card {

	private final String name;
	private ArrayList<Effect> instantEffect;
	private ArrayList<Effect> permanentEffect; 
	
	private ArrayList<JsonValue> payloadInfo; // need by effect type CHANGE
	
	private HashSet<String> permanentEffectType;
	private HashSet<String> instantEffectType;
	
	public Card(String name){
		this.name = name;
		this.instantEffect = new ArrayList<Effect>();
		this.permanentEffect = new ArrayList<Effect>();
		this.payloadInfo = new ArrayList<JsonValue>();
		this.permanentEffectType = new HashSet<String>();
		this.instantEffectType = new HashSet<String>();
	}
		
	public String getName(){
		return this.name;
	}
	
	public void addPayload(JsonValue payload){
		this.payloadInfo.add(payload);
	}
	
	public ArrayList<JsonValue> getPayloadInfo(){
		return this.payloadInfo;
	}
	
	public HashSet<String> getPermanentEffectType(){
		return this.permanentEffectType;
	}
	
	public ArrayList<Effect> getInstantEffect(){
		return this.instantEffect;
	}
	
	public ArrayList<Effect> getPermanentEffect(){
		return this.permanentEffect;
	}
	
	public void registerInstantEffectType(String effectType){
		this.instantEffectType.add(effectType);
	}
	
	public void registerInstantEffect(Effect e){
		this.instantEffect.add(e);
	}
	
	public void registerPermanentEffectType(String effectType){
		this.permanentEffectType.add(effectType);
	}
	
	public void registerPermanentEffect(Effect e){
		this.permanentEffect.add(e);
	}
}