package it.polimi.ingsw.GC_32.Client.Game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.Json;


public class ClientBoardTest {

	public ClientBoard cb;
	
	@Before
	public void initTest(){		
		String boardPacket = "{\"region\":\"[{\\\"BONUS\\\":\\\"#\\\",\\\"REGIONID\\\":0,\\\"SPACEID\\\":0,\\\"SINGLE\\\":true},"
				+ "{\\\"BONUS\\\":\\\"#\\\",\\\"REGIONID\\\":1,\\\"SPACEID\\\":0,\\\"SINGLE\\\":false}]\"}";
		this.cb = new ClientBoard(Json.parse(boardPacket).asObject());
	}
	
	@Test
	public void checkGetExcommunicationCards(){
		assertNotNull(cb.getExcommunicationCards());
	}
	
	@Test
	public void checkFlushFamilyMember(){
		cb.getRegionList().get(0).getActionSpaceList().get(0).addFamilyMember(new ClientFamilyMember());		
		cb.flushFamilyMember();		
		cb.getRegionList().forEach(region -> region.getActionSpaceList().forEach((ClientActionSpace c) -> {
			assertTrue(c.getOccupants().size()==0);
		}));
	}
	
	@Test
	public void checkSetDiceValue(){
		cb.setDiceValue(4, 3, 2);
		assertEquals(cb.getDiceValue()[0],4);
		assertEquals(cb.getDiceValue()[1],3);
		assertEquals(cb.getDiceValue()[2],2);
	}
	
	@Test
	public void checkSetExcommunicationCards(){
		String jsonList = "[\"{\\\"name\\\": \\\"Malus Military Points\\\",\\\"period\\\": 1,\\\"instantEffect\\\": \\\"LESSRESOURCE\\\",\\\"instantPayload\\\": "
				+ "[\\\"MILITARY_POINTS\\\"]}\",\"{\\\"name\\\": \\\"Malus Coins\\\",\\\"period\\\": 1,\\\"instantEffect\\\": \\\"LESSRESOURCE\\\",\\\"instantPayload\\\": [\\\"COINS\\\"]}\""
				+ ",\"{\\\"name\\\": \\\"Malus Servants\\\",\\\"period\\\": 1,\\\"instantEffect\\\": \\\"LESSRESOURCE\\\",\\\"instantPayload\\\": [\\\"SERVANTS\\\"]}\"]";	
		
		cb.setExcommunicationCards(Json.parse(jsonList));
		assertTrue(!cb.getExcommunicationCards().isEmpty());		
	}	
	
	@Test
	public void checkGetRegionList(){
		assertNotNull(cb.getRegionList());
	}
}
