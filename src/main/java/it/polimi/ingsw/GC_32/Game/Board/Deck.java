package it.polimi.ingsw.GC_32.Game.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Deck<T> {

	private HashMap<Integer,T> deck;
	
	// inizializza il mazzo con la lista di elementi specificata
	public Deck(List<T> elements){
		this.deck = new HashMap<Integer,T>();
		for(int i=0; i<elements.size(); i++){
			deck.put(i, elements.get(i));
		}
	}
	
	public HashMap<Integer, T> getDeck(){
		return this.deck;
	}
	
	// mischia il mazzo
	public void shuffleDeck(){

	}
	
	public List<T> getElements(){
		return (List<T>) deck.values();
	}
	
	// dati N mazzi di tipo T, ne genera uno unico impilando i mazzi passati come parametri secondo l'ordine con cui essi vengono passati nel metodo
	public Deck<T> composeDeck(Deck<T>...decks){
		List<T> tmp = new ArrayList<T>();
		for(Deck<T> e : decks){
			for(int i=0; i<e.getDeck().size(); i++){
				tmp.add(e.getDeck().get(i));
			}
		}
		return new Deck(tmp);
	}
	
	// pesca il primo elmento in cima al mazzo
	public T drawElement(){
		return null;
	}
	
	// pesca i primi numberOfElementsToDraw elementi in cima al mazzo
	public List<T> drawManyElements(int numberOfElementsToDraw){
		return null;
	}
	
	public T drawRandomElement(){
		return null;
	}
	
}
