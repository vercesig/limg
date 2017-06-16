package it.polimi.ingsw.GC_32.Client.Game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ClientPlayerTest{
	
	public ClientPlayer clientPlayer;
	
	@Before
	public void initTest(){
		this.clientPlayer = new ClientPlayer();
	}
	
	@Test
	public void checkGetResource(){
		assertNotNull(this.clientPlayer.getPlayerResources());
	}
	
	@Test
	public void checkCardAdding(){
		this.clientPlayer.addCard("TEST", "TESTCARD");
		assertTrue(this.clientPlayer.getCards().get("TEST").contains("TESTCARD"));
		this.clientPlayer.addCard("TEST", "TESTCARD2");
		assertEquals(2, this.clientPlayer.getCards().get("TEST").size());
		assertTrue(this.clientPlayer.getCards().get("TEST").contains("TESTCARD2"));
	}
	
	@Test
	public void checkAddResource(){
		ResourceSet rs = new ResourceSet();
		rs.addResource("WOOD", 10);
		this.clientPlayer.addResources(rs);
		assertEquals(10, this.clientPlayer.getPlayerResources().getResource("WOOD"));
	}
	
	@Test
	public void checkToString(){
		this.clientPlayer.addCard("TEST", "TESTCARD");
		assertNotNull(this.clientPlayer.toString());
	}
}