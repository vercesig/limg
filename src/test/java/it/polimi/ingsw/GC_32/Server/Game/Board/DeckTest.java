package it.polimi.ingsw.GC_32.Server.Game.Board;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class DeckTest{
	public Deck<String> deck;
	
	@Test
	public void checkDeckNotNull(){
		this.deck = new Deck<String>();
		assertNotNull(this.deck.getDeck());
	}
	
	@Test
	public void checkDeck(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("Test");
		list.add("Test String 2");
		this.deck = new Deck<String>(list);
		assertTrue(this.deck.getDeck().containsAll(list));
		ArrayList<String> list2 = new StringGenerator().generateStringList(100);
 		@SuppressWarnings("unchecked")
		Deck<String> testDeck = new Deck<String>(new Deck<String>(list), new Deck<String>(list2));
 		list.addAll(list2);
 		assertTrue(testDeck.getDeck().containsAll(list));
	}
	
	@Test
	public void checkShuffle(){
		ArrayList<String> list = new StringGenerator().generateStringList(100);
		int corr = 0;
		for(int i = 0; i < 1000; i++){
			this.deck = new Deck<String>(list);
			if(_checkShuffle(this.deck, list)){
				corr++;
			}
		}
		assertTrue(corr > 800);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void checkCompose(){
		ArrayList<String> list = new StringGenerator().generateStringList(100);
		ArrayList<String> list2 = new StringGenerator().generateStringList(100);
		this.deck = new Deck<String>();
		this.deck.addDecks(new Deck<String>(list), new Deck<String>(list2));
		list.addAll(list2);
		assertTrue(this.deck.getDeck().containsAll(list));
	}
	
	@Test
	public void checkRandomDraw(){
		ArrayList<String> list = new StringGenerator().generateStringList(100000);
		this.deck = new Deck<String>(list);
		int oop = 0;
		for(int i = 0; i < 1000; i++){
			if(!this.deck.drawRandomElement().equals(list.get(1))){
				oop++;
			}
		}
		assertTrue(oop > 800);
	}
	
	@Test
	public void checkMultiDraw(){
		ArrayList<String> list = new StringGenerator().generateStringList(100);
		this.deck = new Deck<String>(list);
		List<String> generatedDeck = this.deck.drawManyElements(10);
		assertEquals(10, generatedDeck.size());
		for(String element: generatedDeck){
		    assertEquals(true, list.contains(element));
		}
	}
	
	@Test
	public void checkToStringNotNull(){
		this.deck = new Deck<String>();
		assertNotNull(this.deck.toString());
		this.deck.getDeck().add("Test");
		assertNotNull(this.deck.toString());
	}
	
	private Boolean _checkShuffle(Deck<String> testDeck, ArrayList<String> normal){
		testDeck.shuffleDeck();
		assertTrue(testDeck.getDeck().containsAll(normal));
		int i = 0;
		int oop = 0;
		while(true){
			try{
				String string = testDeck.drawElement();
				if( !string.equals(normal.get(i)) ){
					oop++;
				}
			}
			catch(IndexOutOfBoundsException e){
				break;
			}
			i++;
		}
		return oop > (normal.size()*0.5);
	}

	private class StringGenerator{
	
		public String generateString(){
			return new BigInteger(240, new Random()).toString(64);
		}
		
		public ArrayList<String> generateStringList(int length){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0; i < length; i++){
				list.add(generateString());
			}
			return list;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void generalCheck(){
		
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
		
		Deck<Integer> c = new Deck<Integer>(a);
		System.out.println("..................");
		c.shuffleDeck();
		System.out.println(c.toString());
		System.out.println("pesco elemento in cima al mazzo :" +c.drawElement().toString());
		System.out.println("pesco elemento in cima al mazzo :" +c.drawElement().toString());
		System.out.println("pesco i primi 5 elementi in cima al mazzo :" +c.drawManyElements(5).toString());
		System.out.println("pesco elemento casuale :" +c.drawRandomElement().toString());


		Deck<Integer> d = new Deck<Integer>();
		Deck<Integer> f = new Deck<Integer>();
		Deck<Integer> g = new Deck<Integer>();
		g.addDecks(f,d);
		System.out.println(g.toString());
		
		
	}
}