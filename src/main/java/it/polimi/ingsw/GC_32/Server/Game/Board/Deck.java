package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Deck is a generic class which is used to modelize a deck of cards. Deck offers all the generic operations which can be done with a real deck, such as draw the element on
 * the top of the deck, shuffle the deck or draw the first N elements from the top of the deck. 
 * 
 * Passing as parameter the Card class, or one of his child classes, Deck can be used to generate a real deck of cards. conventionally the element with index 0 is the 
 * card on the top of the deck.
 *
 *<ul>
 *<li>{@link #deck}: an ArrayList of generic type T used to realize the deck structure</li>
 *</ul>
 *
 * @param <T> the type of cards which compose this deck
 */
public class Deck<T> {
		
	private ArrayList<T> deck;
	
	/**
	 * initialize the deck as an empty deck
	 */
	public Deck(){
		this.deck = new ArrayList<T>();
	}
	
	/**
	 * initialize the deck with the list of element passed as argument
	 * @param elements the generic list of type T which must be structured as deck
	 */
	public Deck(List<T> elements){
		this.deck = new ArrayList<T>();
		for(T e : elements){
			deck.add(e);
		}
	}
	
	/**
	 * initialize a deck stacking up an array of decks
	 * @param decks the array of decks to steck up
	 */
	@SuppressWarnings("unchecked")
	public Deck(Deck<T>... decks){
		this();
		this.addDecks(decks);
	}
	
	/**
	 * allows to retrive the deck
	 * @return the deck
	 */
	public ArrayList<T> getDeck(){
		return this.deck;
	}
	
	/**
	 * allows to shuffle the deck
	 */
	public void shuffleDeck(){
		ArrayList<T> shuffledDeck = new ArrayList<T>();
		
		Random randomGenerator = new Random();
		int startDimension = deck.size();
		
		for(int i=0; i<startDimension; i++){
			int randomNumber = randomGenerator.nextInt(deck.size());
			shuffledDeck.add(this.deck.get(randomNumber));
			this.deck.remove(randomNumber);
		}
		deck = shuffledDeck;
	}
	
	/**
	 * given a deck, addDeck() takes all the element of the deck passed as argument and add it to the interal ArrayList deck
	 * @param deck the deck to add to this deck
	 */
	public void addDeck(Deck<T> deck){
		this.deck.addAll(deck.getDeck());
	}
	
	/**
	 * given an array of decks, addDeck() takes all the element of the decks passed as argument and add it to the interal ArrayList deck, following the order the decks
	 * appear into the array
	 * @param decks the array of deck to add to this deck
	 */
	@SuppressWarnings("unchecked")
	public void addDecks(Deck<T>...decks){
		for(Deck<T> deck: decks){
			this.addDeck(deck);
		}
	}
	
	/**
	 * draw the first element of this deck
	 * @return the element on the top of this deck
	 */
	public T drawElement(){
		T drewElement = deck.get(0);
		deck.remove(0);
		return drewElement;
	}
	
	/**
	 * draw the first numberOfElementsToDraw elements from the top of this deck
	 * @param numberOfElementsToDraw the number of elements to draw from the top of this deck
	 * @return a list of element drew from the top of this deck
	 */
	@SuppressWarnings("unchecked")
    public List<T> drawManyElements(int numberOfElementsToDraw){
		List<T> drewElements = new ArrayList<T>();
		ArrayList<T> tmpList = (ArrayList<T>) deck.clone();
		Collections.shuffle(tmpList);
		for(int i=0; i<numberOfElementsToDraw; i++){
			drewElements.add(tmpList.get(0));
			tmpList.remove(0);
		}
		return drewElements;
	}
	
	/**
	 * randomly draw a card from this deck
	 * @return a randomly drew element from the deck
	 */
	public T drawRandomElement(){
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(deck.size());
		T drewElement = deck.get(randomNumber);
		deck.remove(randomNumber);
		return drewElement;
	}
	
	/**
	 * return the string representation of the deck
	 */
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(T e : deck){
			tmp.append(e.toString()+"\n");
		}
		return new String(tmp);
	}
}