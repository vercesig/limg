package it.polimi.ingsw.GC_32.Client.Game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;


public class ClientBoardTest {

	public ClientBoard cb;
	
	@Before
	public void initTest(){
		String boardPacket = "{\"region\":\"[{\\\"BONUS\\\":\\\"#\\\",\\\"REGIONID\\\":0,\\\"SPACEID\\\":0,\\\"SINGLE\\\":true}," +
		                     "{\\\"BONUS\\\":\\\"#\\\",\\\"REGIONID\\\":1,\\\"SPACEID\\\":0,\\\"SINGLE\\\":false}]\"}";
		JsonObject boardObject = new JsonObject();
		this.cb = new ClientBoard(boardObject.add("Market", new JsonArray().add(new JsonObject().add("BONUS", "#").add("REGIONID", 0).add("SPACEID", 0).add("SINGLE", true))
		                                                                   .add(new JsonObject().add("BONUS", "#").add("REGIONID", 0).add("SPACEID", 1).add("SINGLE", true)).toString())
		                                     .add("Harvest", new JsonArray().add(new JsonObject().add("BONUS", "#").add("REGIONID", 1).add("SPACEID", 0).add("SINGLE", true))
		                                                                    .add(new JsonObject().add("BONUS", "#").add("REGIONID", 1).add("SPACEID", 1).add("SINGLE", false)).toString())
		                                     .add("Production", new JsonArray().add(new JsonObject().add("BONUS", "#").add("REGIONID", 2).add("SPACEID", 0).add("SINGLE", true)).toString())
		                                     .add("Priviledge", new JsonArray().add(new JsonObject().add("BONUS", "#").add("REGIONID", 3).add("SPACEID", 0).add("SINGLE", false)).toString())
		                                     .add("Tower1", new JsonArray().add(new JsonObject().add("BONUS", "#").add("REGIONID", 4).add("SPACEID", 0).add("SINGLE", true)).toString())
		                                     .add("Tower2", new JsonArray().add(new JsonObject().add("BONUS", "#").add("REGIONID", 5).add("SPACEID", 0).add("SINGLE", true)).toString())
		                         );
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
	
	@Test
	public void checkToStringNotNull(){
	    assertNotNull(cb.toString());
	}
}
