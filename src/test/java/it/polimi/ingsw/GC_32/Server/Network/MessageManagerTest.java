package it.polimi.ingsw.GC_32.Server.Network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.eclipsesource.json.Json;

import org.junit.Rule;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class MessageManagerTest {
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void chekMessageManagerNotNull(){
		assertNotNull(MessageManager.getInstance());
	}

	@Test
	public void checkPutRecivedMessage(){
		String message = "test message";
		UUID gameID = UUID.randomUUID();
		UUID playerID = UUID.randomUUID();
		Game game = mock(Game.class);
		when(game.getLock()).thenReturn(playerID);
		when(game.getUUID()).thenReturn(gameID);
		GameRegistry.getInstance().registerGame(game);
		MessageManager.getInstance().registerGame(game);
		GameMessage testGameMessage = new GameMessage(gameID, playerID, "TESTMSG", Json.value(message));
		MessageManager.getInstance().putRecivedMessage(testGameMessage);
		
		assertTrue(MessageManager.getInstance().getQueueForGame(gameID).contains(testGameMessage));
	}
	
	@Test
	public void checkSendMessage(){
		Player testPlayerSocket = new Player();
		String messageSocket = "test message";
		
		Player testPlayerRMI = new Player();
		String messageRMI = "test message";
		
		GameRegistry.getInstance().registerPlayer(testPlayerSocket, ConnectionType.SOCKET);
		GameRegistry.getInstance().registerPlayer(testPlayerRMI, ConnectionType.RMI);
		
		GameMessage testGameMessageSocket = new GameMessage(null, testPlayerSocket.getUUID(),null, Json.value(messageSocket));
		MessageManager.getInstance().sendMessge(testGameMessageSocket);
		assertTrue(MessageManager.getInstance().getSocketSendQueue().contains(testGameMessageSocket));
		
		GameMessage testGameMessageRMI = new GameMessage(null, testPlayerRMI.getUUID(), null, Json.value(messageRMI));
		MessageManager.getInstance().sendMessge(testGameMessageRMI);
		assertTrue(MessageManager.getInstance().getRMISendQueue().contains(testGameMessageRMI));		
	}
	
	@SuppressWarnings("deprecation")
    @Test
	public void checkHasMessage(){
		String message = "test message";
		MessageManager.getInstance().chatManagerThread.stop();
		
		GameMessage testGameMessage = new GameMessage(UUID.randomUUID(), UUID.randomUUID(),"MSG", Json.value(message));
		MessageManager.getInstance().putRecivedMessage(testGameMessage);
		
		assertTrue(MessageManager.getInstance().getCommonReceiveQueue().size() > 0);
	}
	
	@Test
	public void checkGetSocketSendQueue(){
		assertNotNull(MessageManager.getInstance().getSocketSendQueue());
	}
	
	@Test
	public void checkGetRMISendQueue(){
		assertNotNull(MessageManager.getInstance().getRMISendQueue());
	}
	
	@Test
	public void checkGetRecivedQueue(){
		assertNotNull(MessageManager.getInstance().getCommonReceiveQueue());
	}
	
	
}
