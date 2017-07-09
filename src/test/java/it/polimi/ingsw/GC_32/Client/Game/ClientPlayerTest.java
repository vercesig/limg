package it.polimi.ingsw.GC_32.Client.Game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
	public void checkToString(){
		this.clientPlayer.addCard("TEST", "TESTCARD");
		assertNotNull(this.clientPlayer.toString());
	}
	
	@Test
	public void checkSetName(){
		clientPlayer.setName("example name");
		assertEquals(clientPlayer.getName(),"example name");
		for(int i=0; i<clientPlayer.getFamilyMembers().length; i++){
			assertEquals(clientPlayer.getFamilyMembers()[i].getOwner(),"example name");
		}
	}
	
	@Test
	public void checkGetFamilyMembers(){
		assertNotNull(this.clientPlayer.getFamilyMembers());
	}
	
	@Test
	public void checkGetTrack(){
		assertNotNull(this.clientPlayer.getTrack());
	}
	
	@Test
	public void checkGetCard(){
		assertNotNull(this.clientPlayer.getCards());
	}
}