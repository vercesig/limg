package it.polimi.ingsw.GC_32.Server.Game.Card;

import java.util.HashMap;
import java.util.Set;

import it.polimi.ingsw.GC_32.Server.Game.Board.Deck;

/**
 * singleton class which contains all the card of the game. Card are organized in the CardRegister as decks, each labeled by the card type it contains.
 * CardRegistry is setted by Setup class which fill the registry with all the card of the game, when the server starts.
 * 
 * <ul>
 * <li>{@link #instance}: the card registry instance</li>
 * <li>{@link #developmentCardDecks}: HashMap containing the development card decks, each deck is referenced by the type of card it contains</li>
 * <li>{@link #excommunicationCardDecks}: HashMap containing thw excommunication card decks. each deck is referenced by the period of the excommuncation cards it contains</li>
 * <li>{@link #leaderDeck}: the leader card deck</li>
 * </ul>
 *
 * @see Deck, DevelopmenCard, ExcommunicationCard, LeaderCard
 */
public class CardRegistry {

	private static CardRegistry instance;
	private HashMap<String,Deck<DevelopmentCard>> developmentCardDecks;
	private HashMap<Integer,Deck<ExcommunicationCard>> excommunicationCardDecks;
	private Deck<LeaderCard> leaderDeck;
	
	/**
	 * setup the data structures to support the decks
	 */
	private CardRegistry(){
		this.developmentCardDecks = new HashMap<String, Deck<DevelopmentCard>>();
		this.excommunicationCardDecks = new HashMap<Integer, Deck<ExcommunicationCard>>();
		this.leaderDeck = new Deck<LeaderCard> ();
	}
	
	/**
	 * allows to retrive the registry
	 * @return the instance of CardRegistry singleton
	 */
	public static CardRegistry getInstance(){
		if(instance==null){
			instance = new CardRegistry();
		}
		return instance;
	}
	
	/**
	 * allows to register a DevelopmentCard deck
	 * @param deckType the type of deck which must be registered
	 * @param deck the deck to register
	 */
	public void registerDeck(String deckType, Deck<DevelopmentCard> deck){
		this.developmentCardDecks.put(deckType, deck);
	}
	
	/**
	 * allows to register the LeaderCard deck
	 * @param deck the deck to register
	 */
	public void registerDeck(Deck<LeaderCard> deck){
		this.leaderDeck = deck;
	}
	
	/**
	 * allows to register an ExcommunicationCard Deck
	 * @param deckPeriod the period of the deck which must be registered
	 * @param deck the deck to register
	 */
	public void registerDeck(Integer deckPeriod, Deck<ExcommunicationCard> deck){
		this.excommunicationCardDecks.put(deckPeriod, deck);
	}
	
	/**
	 * get a DevelopmentCard deck
	 * @param deckType the type of the deck which must be retrived
	 * @return the deck with the specified card type
	 */
	public Deck<DevelopmentCard> getDeck(String deckType){
		return developmentCardDecks.get(deckType);
	}
	
	/**
	 * get an ExcommunicationCard deck
	 * @param deckPeriod the period of the deck which must be retrived
	 * @return the deck with the specified period
	 */
	public Deck<ExcommunicationCard> getDeck(Integer deckPeriod){
		return excommunicationCardDecks.get(deckPeriod);
	}
	
	/**
	 * get the LeaderCard deck
	 * @return the LeaderCard deck
	 */
	public Deck<LeaderCard> getLeaderDeck(){
		return leaderDeck;
	}
	
	/**
	 * allow to retrive a set containig all the type of DevelopmentCard memorized into the registry
	 * @return Set containing all the type of DevelopmentCard
	 */
	public Set<String> getAllCardType(){
		return this.developmentCardDecks.keySet();
	}
	
	/**
	 * get all the DevelopmentCard deck
	 * @return the HashMap developmentCardDecks
	 */
	public HashMap<String,Deck<DevelopmentCard>> getDevelopmentDecks(){
		return this.developmentCardDecks;
	}
	
}
