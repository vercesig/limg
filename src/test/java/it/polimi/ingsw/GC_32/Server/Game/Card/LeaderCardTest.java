package it.polimi.ingsw.GC_32.Server.Game.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Server.Game.Board.Board;
import it.polimi.ingsw.GC_32.Server.Game.Player;
import it.polimi.ingsw.GC_32.Server.Game.Action;
import it.polimi.ingsw.GC_32.Server.Game.ContextManager;
import it.polimi.ingsw.GC_32.Server.Game.Effect.Effect;

public class LeaderCardTest {
	public LeaderCard card;
	
	@Before
	public void initTest(){
		this.card = new LeaderCard("TestCard", new JsonObject());
	}

	@Test
	public void checkConstructor(){
		assertEquals(false, this.card.isOnTheGame());
		assertEquals(false, this.card.hasATokenAbility());
	}
	
	@Test
	public void checkFlagEffect(){
		Effect testEffect = (Board b, Player p, Action a, ContextManager cm) -> {};
		assertEquals(null, this.card.getFlagEffect());
		this.card.registerFlagEffect(testEffect);
		assertEquals(testEffect, this.card.getFlagEffect());
	}
	
	@Test
	public void checkPlayCard(){
		this.card.playCard();
		assertEquals(true, this.card.isOnTheGame());
		assertEquals(true, this.card.hasATokenAbility());
	}
	
	
	@Test
	public void checkToStringNotNull(){
		assertNotNull(this.card.toString());
	}
}
