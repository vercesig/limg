package it.polimi.ingsw.GC_32.Client.Game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.Json;

public class ClientRegionTest {

	public ClientRegion cr;
	
	@Before
	public void initTest(){
		String type = "TOWER";
		String actionspaces = "[{\"BONUS\":\"#\",\"REGIONID\":0,\"SPACEID\":0,\"SINGLE\":true},"
				+ "{\"BONUS\":\"#\",\"REGIONID\":1,\"SPACEID\":0,\"SINGLE\":false}]";
		cr = new ClientRegion(type,Json.parse(actionspaces).asArray());
	}
	
	@Test
	public void checkGetActionSpaceList(){
		assertNotNull(cr.getActionSpaceList());
	}
	
	@Test
	public void checkGetType(){
		assertEquals(cr.getType(),"TOWER");
	}
	
	@Test
	public void checkToString(){
		assertNotNull(cr.toString());
	}
	
	@Test
	public void checkFlushFamilyMember(){
		cr.getActionSpaceList().get(0).addFamilyMember(new ClientFamilyMember());
		assertTrue(!cr.getActionSpaceList().get(0).getOccupants().isEmpty());
		cr.flushFamilyMember();
		assertTrue(cr.getActionSpaceList().get(0).getOccupants().isEmpty());
	}
	
}
