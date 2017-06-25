package it.polimi.ingsw.GC_32.Server.Game.Card;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class CardTest {
	public Card card;
	
	@Test
	public void checkName(){
		this.card = new Card("NAME");
		assertEquals("NAME", this.card.getName());
	}

	@Test
	public void checkInstantEffect(){
		this.card = new Card("NAME");
		ByteArrayOutputStream printStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(printStream));
		
		Effect e = (p, b, a) -> {
			System.out.print("Hello, this is an instant Effect");
		};
		this.card.registerInstantEffect(e);
		this.card.getInstantEffect().apply(null, null, null);
		assertEquals("Hello, this is an instant Effect", printStream.toString());
	}
	@Test
	public void checkPermanentEffect(){
		this.card = new Card("NAME");
		ByteArrayOutputStream printStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(printStream));
		
		Effect e = (p, b, a) -> {
			System.out.print("Hello, this is a permanent Effect");
		};
		this.card.registerPermanentEffect(e);
		this.card.getPermanentEffect().apply(null, null, null);
		assertEquals("Hello, this is a permanent Effect", printStream.toString());
	}
}
