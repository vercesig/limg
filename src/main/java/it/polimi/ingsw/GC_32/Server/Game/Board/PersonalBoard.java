package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.LinkedList;

import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Card.CardType;
import it.polimi.ingsw.GC_32.Server.Game.Card.Card;

public class PersonalBoard {
	//private final PersonalBonusTile personalBonusTile;
	private final Player owner;
	
	private LinkedList<Card> ventureCards;
	private LinkedList<Card> characterCards;
	private LinkedList<Card> territoryCards;
	private LinkedList<Card> buildingCards;
	
	
	public PersonalBoard(Player owner){
		this.owner = owner;
		
		//this.personalBonusTile = new PersonalBonusTile();
		
		this.ventureCards = new LinkedList<Card>();
		this.characterCards = new LinkedList<Card>();
		this.territoryCards = new LinkedList<Card>();
		this.buildingCards = new LinkedList<Card>();
		
	}


	
/*	public PersonalBonusTile getPersonalBonusTile(){
		return this.personalBonusTile;
	}*/
	
	public LinkedList<Card> getVentureCards(){
		return this.ventureCards;
	}
	
	public LinkedList<Card> getBuildingCards(){
		return this.buildingCards;
	}
	
	public LinkedList<Card> getTerritoryCards(){
		return this.territoryCards;
	}
	
	public LinkedList<Card> getCharacterCards(){
		return this.characterCards;
	}
	
	public void addToVentureCards(){
		
	}
	
	public void addToBuildingCards(){
		
	}
	
	public void addToTerritoryCards(){
		
	}
	
	public void addToCharacterCards(){
		
	}
	
}
