package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;
import java.util.HashSet;

import com.eclipsesource.json.JsonValue;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class Card {

	private final String name;
	private ArrayList<Effect> instantEffect;
	private ArrayList<Effect> permanentEffect; 
	private ArrayList<JsonValue> payloadInfo;
	private HashSet<String> permanentEffectType;
	
	public Card(String name){
		this.name = name;
		this.instantEffect = new ArrayList<Effect>();
		this.permanentEffect = new ArrayList<Effect>();
		this.payloadInfo = new ArrayList<JsonValue>();
		this.permanentEffectType = new HashSet<String>();
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
		try{
		return this.instantEffect;		
		}
		catch(NullPointerException e){
			return null;
		}
	}
	
	public ArrayList<Effect> getPermanentEffect(){
		try{
		return this.permanentEffect;
		}
		catch(NullPointerException e){
			return null;
		}
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