package it.polimi.ingsw.GC_32.Server.Game.Card;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class Card {

	private final String name;
	private Effect instantEffect;
	private Effect permanentEffect; 
	
	public Card(String name){
		this.name = name;
	}
		
	public String getName(){
		return this.name;
	}
	
	public Effect getInstantEffect(){
		try{
		return this.instantEffect;		
		}
		catch(NullPointerException e){
			return null;
		}
	}
	
	public Effect getPermanentEffect(){
		try{
		return this.permanentEffect;
		}
		catch(NullPointerException e){
			return null;
		}
	}
	
	public void registerInstantEffect(Effect e){
		this.instantEffect = e;
	}
	
	public void registerPermanentEffect(Effect e){
		this.permanentEffect = e;
	}
	

}