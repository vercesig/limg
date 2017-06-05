package it.polimi.ingsw.GC_32.Server.Game.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LeaderCardTest {
	public LeaderCard card;
	
	@Test
	public void checkIsDiscarded(){
		this.card = new LeaderCard("NAME", true);
		assertEquals(false, this.card.isDiscarded());
	}
	@Test
	public void checkDiscard(){
		this.card = new LeaderCard("NAME", true);
		this.card.discard();
		assertEquals(true, this.card.isDiscarded());
	}	
	@Test
	public void checkGetOnePerRoundAbility(){
		this.card = new LeaderCard("NAME", true);
		assertNotNull(this.card.getOnePerRoundAbility());
	}
	@Test
	public void checkTurnCard(){
		this.card = new LeaderCard("NAME", true);
		this.card.turnCard();
		LeaderCard cardOne = new LeaderCard("NAME", true);
		for (int i=0; i<100; i++){
			cardOne.turnCard();
		}
		LeaderCard cardTwo= new LeaderCard("NAME", true);
		for (int i=0; i<99; i++){
			cardTwo.turnCard();
		}
		assertEquals(false, this.card.getOnePerRoundAbility());
		assertEquals(true, cardOne.getOnePerRoundAbility());
		assertEquals(false, cardTwo.getOnePerRoundAbility());
	}
}
