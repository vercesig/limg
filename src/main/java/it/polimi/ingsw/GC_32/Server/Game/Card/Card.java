package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.ArrayList;

import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class Card {

	private final String name;
	private boolean onTheGame;
	private ArrayList<String> instantEffects;
	private ArrayList<String> permanentEffects;
	
	
	public Card(String name){
		this.name = name;
		this.instantEffects = new ArrayList<String>();
		this.permanentEffects = new ArrayList<String>();
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
