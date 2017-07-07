package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck<T> {

	/**
	 * l'elemento di indice 0 è il primo elemento del mazzo (quello in cima)
	 * nell'ambito del gioco, i mazzi andranno impliati secondo l'ordine I periodo, II periodo, III periodo. dunque si effettuerà la chiamata
	 * composeDeck(Iperiodo,IIperiodo,IIIperiodo)
	 * 
	 * */
		
	private ArrayList<T> deck;
	
	public Deck(){
		this.deck = new ArrayList<T>();
	}
	
	public Deck(List<T> elements){
		this.deck = new ArrayList<T>();
		for(T e : elements){
			deck.add(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Deck(Deck<T>... decks){
		this();
		this.addDecks(decks);
	}
	
	public ArrayList<T> getDeck(){
		return this.deck;
	}
	
	// mischia il mazzo
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
	
	public void addDeck(Deck<T> deck){
		this.deck.addAll(deck.getDeck());
	}
	
	@SuppressWarnings("unchecked")
	public void addDecks(Deck<T>...decks){
		for(Deck<T> deck: decks){
			this.addDeck(deck);
		}
	}
	
	// pesca il primo elmento in cima al mazzo
	public T drawElement(){
		T drewElement = deck.get(0);
		deck.remove(0);
		return drewElement;
	}
	
	// pesca i primi numberOfElementsToDraw elementi dalla cima del mazzo
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
	
	// pesca casulamente una carta dal mazzo
	public T drawRandomElement(){
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(deck.size());
		T drewElement = deck.get(randomNumber);
		deck.remove(randomNumber);
		return drewElement;
	}
	
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		for(T e : deck){
			tmp.append(e.toString()+"\n");
		}
		return new String(tmp);
	}
}