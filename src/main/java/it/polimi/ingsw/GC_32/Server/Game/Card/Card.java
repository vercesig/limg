package it.polimi.ingsw.GC_32.Server.Game.Card;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class Card {

	private final String name;
	private boolean onTheGame;
	private Effect instantEffect;
	private Effect permanentEffect; 
	
	public Card(String name){
		this.name = name;
		this.onTheGame = false;
	}
		
	public String getName(){
		return this.name;
	}
	
	public boolean isOnTheGame(){
		return this.onTheGame;
	}
	
	public Effect getInstantEffect(){
		return this.instantEffect;		
	}
	
	public Effect getPermanentEffect(){
		return this.permanentEffect;
	}
	
	public void registerInstantEffect(Effect e){
		this.instantEffect = e;
	}
	
	public void registerPermanentEffect(Effect e){
		this.permanentEffect = e;
	}
	

}