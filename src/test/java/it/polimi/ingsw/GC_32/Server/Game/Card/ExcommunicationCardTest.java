package it.polimi.ingsw.GC_32.Server.Game.Card;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExcommunicationCardTest {
	public ExcommunicationCard card;

	@Test
	public void checkGetPeriod(){
		this.card = new ExcommunicationCard("NAME", 0);
		assertEquals(0, this.card.getPeriod());
	}
	@Test
	public void checkToString(){
		this.card = new ExcommunicationCard("NAME", 0);
		assertEquals("name: NAME\nperiod: 0\n", this.card.toString());
	}
}
