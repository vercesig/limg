package it.polimi.ingsw.GC_32.Client.Game;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClientFamilyMemberTest{
	
	@Test
	public void checkToString(){
		ClientFamilyMember cfm = new ClientFamilyMember();
		assertNotNull(cfm.toString());
	}
}