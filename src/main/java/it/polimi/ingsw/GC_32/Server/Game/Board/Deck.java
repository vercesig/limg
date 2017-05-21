package it.polimi.ingsw.GC_32.Server.Game.Board;

import java.util.ArrayList;
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
	
	public Deck(List<T> elements){
		this.deck = new ArrayList<T>();
		for(T e : elements){
			deck.add(e);
		}
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
		
		StringBuilder tmp = new StringBuilder();
		for(T e : shuffledDeck){
			tmp.append(e.toString());
		}
		
		deck = shuffledDeck;
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
		T drewElement = deck.get(0);
		deck.remove(0);
		return drewElement;
	}
	
	// pesca i primi numberOfElementsToDraw elementi dalla cima del mazzo
	public List<T> drawManyElements(int numberOfElementsToDraw){
		List<T> drewElements = new ArrayList<T>();
		for(int i=0; i<numberOfElementsToDraw; i++){
			drewElements.add(deck.get(0));
			deck.remove(0);
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
	
	public static void main(String args[]){
		
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(10);
		a.add(15);
		a.add(7);
		a.add(48);
		a.add(210);
		a.add(215);
		a.add(73);
		a.add(4821);
		a.add(105);
		a.add(1544);
		a.add(768);
		a.add(48888);
		
		for(Integer b : a){
			System.out.println(b.toString());
		}
		
		Deck<Integer> c = new Deck(a);
		System.out.println("..................");
		c.shuffleDeck();
		System.out.println(c.toString());
		System.out.println("pesco elemento in cima al mazzo :" +c.drawElement().toString());
		System.out.println("pesco i primi 5 elementi in cima al mazzo :" +c.drawManyElements(5).toString());
		System.out.println("pesco elemento casuale :" +c.drawRandomElement().toString());
		
		
	}
	
}