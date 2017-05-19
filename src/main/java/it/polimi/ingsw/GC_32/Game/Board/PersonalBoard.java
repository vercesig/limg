package it.polimi.ingsw.GC_32.Game.Board;

import java.util.LinkedList;

import it.polimi.ingsw.GC_32.Game.Card;
import it.polimi.ingsw.GC_32.Game.Player;
import it.polimi.ingsw.GC_32.Game.Resource;

public class PersonalBoard {

	private Resource wood;
	private Resource stone;
	private Resource coins;
	private Resource servants;
	private final PersonalBonusTile personalBonusTile;
	private final Player owner;
	
	private LinkedList<Card> ventureCards;
	private LinkedList<Card> characterCards;
	private LinkedList<Card> territoryCards;
	private LinkedList<Card> buildingCards;
	
	
	public PersonalBoard(Player owner){
		
		this.owner = owner;
		
		this.resources = new Resource();
		
		this.personalBonusTile = new PersonalBonusTile();
		
		this.ventureCards = new LinkedList<Card>();
		this.characterCards = new LinkedList<Card>();
		this.territoryCards = new LinkedList<Card>();
		this.buildingCards = new LinkedList<Card>();
		
	}

	public Resource getWoodQuantity() {
		return this.wood;
	}

	public Resource getStoneQuantity() {
		return this.stone;
	}

	public Resource getCoins() {
		return this.coins;
	}

	public Resource getServants() {
		return this.servants;
	}
	
	public PersonalBonusTile getPersonalBonusTile(){
		return this.personalBonusTile;
	}
	
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
