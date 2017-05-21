package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class Card {

	private final String name;
	private boolean onTheGame;
	private ArrayList<Effect> instantEffects;
	private ArrayList<Effect> permanentEffects;
	
	
	public Card(String name){
		this.name = name;
		this.instantEffects = new ArrayList<Effect>();
		this.permanentEffects = new ArrayList<Effect>();
		this.onTheGame = false;
	}
		
	public String getName(){
		return this.name;
	}
	
	public boolean isOnTheGame(){
		return this.onTheGame;
	}
	
	public void activateInstantEffect(){
		
	}
	
	public void activatePermanentEffect(){
		
	}
		
}
