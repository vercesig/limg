package it.polimi.ingsw.GC_32.Server.Game;

import java.util.HashMap;
import java.util.Set;

import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;
import it.polimi.ingsw.GC_32.Server.Game.Card.DevelopmentCard;
import it.polimi.ingsw.GC_32.Server.Game.Card.ExcommunicationCard;

public class CardRegistry {

	private static CardRegistry instance;
	private HashMap<String,Deck<DevelopmentCard>> developmentCardDecks;
	private HashMap<Integer,Deck<ExcommunicationCard>> excommunicationCardDecks;
	
	
	private CardRegistry(){
		this.developmentCardDecks = new HashMap<String, Deck<DevelopmentCard>>();
		this.excommunicationCardDecks = new HashMap<Integer, Deck<ExcommunicationCard>>();
	}
	
	public static CardRegistry getInstance(){
		if(instance==null){
			instance = new CardRegistry();
		}
		return instance;
	}
	
	public void registerDeck(String deckType, Deck<DevelopmentCard> deck){
		this.developmentCardDecks.put(deckType, deck);
	}
	
	public void registerDeck(Integer deckPeriod, Deck<ExcommunicationCard> deck){
		this.excommunicationCardDecks.put(deckPeriod, deck);
	}
	
	public Deck<DevelopmentCard> getDeck(String deckType){
		return developmentCardDecks.get(deckType);
	}
	
	public Deck<ExcommunicationCard> getDeck(Integer deckPeriod){
		return excommunicationCardDecks.get(deckPeriod);
	}
	
	public Set<String> getAllCardType(){
		return this.developmentCardDecks.keySet();
	}
	
	public HashMap<String,Deck<DevelopmentCard>> getDevelopmentDecks(){
		return this.developmentCardDecks;
	}
	
}
