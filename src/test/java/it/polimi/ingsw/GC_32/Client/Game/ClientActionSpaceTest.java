package it.polimi.ingsw.GC_32.Client.Game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;

public class ClientActionSpaceTest {

	public ClientActionSpace as;
	
	@Before
	public void initTest(){
		ResourceSet bonus = new ResourceSet();
		bonus.setResource("WOOD", 1);
		this.as = new ClientActionSpace(bonus, 2,true,0,0,false);
	}
	
	@Test
	public void checkSetCard(){
		this.as.setCard("card test");
		assertEquals(as.getCardName(),"card test");
	}
	
	@Test
	public void checkLock(){
		this.as.Lock();
		assertTrue(as.blocked);
	}
	
	@Test
	public void checkUnlock(){
		this.as.Unlock();
		assertFalse(as.blocked);
	}
	
	@Test
	public void checkGetOccupants(){
		assertNotNull(as.getOccupants());
	}
	
	@Test
	public void checkAddFamilyMember(){
		assertTrue(as.getOccupants().isEmpty());
		as.addFamilyMember(new ClientFamilyMember());
		assertFalse(as.getOccupants().isEmpty());
	}
	
	@Test
	public void checkGetInfoContainer(){
		String[] tmp = as.getInfoContainer();
		for(int i=0; i<tmp.length; i++){
			assertNotNull(tmp[i]);
		}
		assertEquals(tmp[0], Integer.valueOf(0).toString());
		assertEquals(tmp[1], Integer.valueOf(0).toString());
		assertEquals(tmp[2], Integer.valueOf(2).toString());
		assertEquals(tmp[3], Boolean.valueOf(true).toString());
		assertEquals(tmp[4], Boolean.valueOf(false).toString());
		assertNotNull(tmp[5]);
		this.as.addFamilyMember(new ClientFamilyMember());
		assertNotNull(tmp[6]);
		assertEquals(tmp[7], "empty");
	}
	
	@Test 
	public void checkToString(){
		assertNotNull(this.as.toString());
	}
	
	@Test
	public void checkFlushFamilyMember(){
		this.as.flushFamilyMember();
		assertEquals(this.as.getOccupants().size(),0);
	}
	
}
