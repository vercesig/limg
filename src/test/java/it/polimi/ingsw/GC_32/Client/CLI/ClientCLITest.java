package it.polimi.ingsw.GC_32.Client.CLI;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public class ClientCLITest {

	public ClientCLI cc;
	
	@Before
	public void initTest(){
		cc = new ClientCLI();
	}
	
	@Test
	public void checkContextNotNull(){
		for(int i=0; i<cc.getContextList().length; i++){
			assertNotNull(cc.getContextList()[i]);
		}
	}
	
	@Test
	public void checkRegisterBoard(){
		String boardPacket = "{\"region\":\"[{\\\"BONUS\\\":\\\"#\\\",\\\"REGIONID\\\":0,\\\"SPACEID\\\":0,\\\"SINGLE\\\":true},"
				+ "{\\\"BONUS\\\":\\\"#\\\",\\\"REGIONID\\\":1,\\\"SPACEID\\\":0,\\\"SINGLE\\\":false}]\"}";
		ClientBoard cb = new ClientBoard(Json.parse(boardPacket).asObject());
		cc.registerBoard(cb);
		assertNotNull(cc.getBoard());
	}
	
	@Test
	public void checkRegisterPlayer(){
		HashMap<String, ClientPlayer> playerList = new HashMap<String, ClientPlayer>();
		playerList.put("PLAYER1", new ClientPlayer());
		playerList.put("PLAYER2", new ClientPlayer());
		
		cc.registerPlayers(playerList);
		assertNotNull(cc.getPlayerList());
	}
	
	@Test
	public void checkRegisterGameUUID(){
		cc.registerGameUUID(UUID.randomUUID().toString());
		assertNotNull(cc.getGameUUID());
	}
	
	@Test
	public void checkRegisterPlayerUUID(){
		cc.registerPlayerUUID(UUID.randomUUID().toString());
		assertNotNull(cc.getPlayerUUID());
	}
	
	@Test
	public void checkSendQueue(){
		cc.registerSendMessageQueue(new ConcurrentLinkedQueue<String>());
		assertNotNull(cc.getSendQueue());
	}
	
	@Test
	public void checkLeaderStartPhaseEnd(){
		cc.leaderStartPhaseEnd();
		assertFalse(cc.leaderStartPhase);
	}
	
	@Test
	public void checkOpenContext(){
		cc.openContext(new JsonObject());
		assertTrue(!cc.contextQueue.isEmpty());
	}
	
	@Test
	public void checkDisplayMessage(){
		cc.displayMessage("test message");
		assertTrue(!cc.messageQueue.isEmpty());
	}
	
	@Test
	public void checkDisplaySendMessage(){		
		cc.displaySendMessage("PLAYERTEST", "hello");
		assertNotNull(cc.out.toString());
	}
	
	@Test
	public void checkDisplayReceiveMessage(){		
		cc.receiveMessage("PLAYERTEST", "hello");
		assertNotNull(cc.out.toString());
	}
	
	@Test
	public void checkWaitTurn(){
		cc.waitTurn(true);
		assertTrue(cc.isWaiting());
	}
	
	@Test
	public void checkProcessContext(){
		String contextPacket = "{\"CONTEXTID\":5,\"PAYLOAD\":{\"LIST\":"
				+ "[\"Michelangelo Buonarroti\",\"Pico della Mirandola\",\"Filippo Brunelleschi\","
				+ "\"Bartolomeo Colleoni\"]}}";
		Thread t = new Thread(cc);
		t.start();
		cc.openContext(Json.parse(contextPacket).asObject());
		assertNotNull(cc.out.toString());
		assertTrue(!cc.contextQueue.isEmpty());
		cc.kill();
	}
	
	@Test
	public void checkProcessMessage() throws InterruptedException{
		String testMessage = "hello";
		Thread t = new Thread(cc);
		t.start();
		cc.displayMessage(testMessage);
		assertNotNull(cc.out.toString());
		Thread.sleep(50);
		assertTrue(cc.messageQueue.isEmpty());
		cc.kill();
	}
}
