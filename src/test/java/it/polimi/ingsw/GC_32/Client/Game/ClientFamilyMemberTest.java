package it.polimi.ingsw.GC_32.Client.Game;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClientFamilyMemberTest{
	
	@Test
	public void checkToString(){
		ClientFamilyMember cfm = new ClientFamilyMember();
		assertNotNull(cfm.toString());
	}
	
	@Test
	public void checkSetName(){
		ClientFamilyMember cfm = new ClientFamilyMember();
		cfm.setName("example name");
		assertNotNull(cfm.getOwner());
	}
	
	@Test
	public void checksetBusyFlag(){
		ClientFamilyMember cfm = new ClientFamilyMember();
		cfm.setBusyFlag(true);
		assertEquals(cfm.isBusy(),true);
		cfm.setBusyFlag(false);
		assertEquals(cfm.isBusy(),false);
	}
	
	@Test
	public void checkSetActionValue(){
		ClientFamilyMember cfm = new ClientFamilyMember();
		cfm.setActionValue(10);
		assertEquals(cfm.actionValue,10);
	}
}