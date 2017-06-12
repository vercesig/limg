package it.polimi.ingsw.GC_32.Server.Network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
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
		
		GameMessage testGameMessage = new GameMessage("Test_UUID","TESTMSG",message);
		MessageManager.getInstance().putRecivedMessage(testGameMessage);
		
		assertTrue(MessageManager.getInstance().getRecivedQueue().contains(testGameMessage));
	}
	
	@Test
	public void checkSendMessage(){
		Player testPlayerSocket = new Player();
		String messageSocket = "test message";
		
		Player testPlayerRMI = new Player();
		String messageRMI = "test message";
		
		PlayerRegistry.getInstance().registerPlayer(testPlayerSocket.getUUID(), ConnectionType.SOCKET);
		PlayerRegistry.getInstance().registerPlayer(testPlayerRMI.getUUID(), ConnectionType.RMI);
		
		GameMessage testGameMessageSocket = new GameMessage(testPlayerSocket.getUUID(),null,messageSocket);
		MessageManager.getInstance().sendMessge(testGameMessageSocket);
		assertTrue(MessageManager.getInstance().getSocketSendQueue().contains(testGameMessageSocket));
		
		GameMessage testGameMessageRMI = new GameMessage(testPlayerRMI.getUUID(),null,messageRMI);
		MessageManager.getInstance().sendMessge(testGameMessageRMI);
		assertTrue(MessageManager.getInstance().getRMISendQueue().contains(testGameMessageRMI));		
	}
	
	@Test
	public void checkHasMessage(){
		Game game = mock(Game.class);
		when(game.getLock()).thenReturn("Test_UUID");
		String message = "test message";
		MessageManager.getInstance().registerGame(game);
		
		GameMessage testGameMessage = new GameMessage("Test_UUID","TSTMSG",message);
		MessageManager.getInstance().putRecivedMessage(testGameMessage);
		
		assertTrue(MessageManager.getInstance().hasMessage());	
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
		assertNotNull(MessageManager.getInstance().getRecivedQueue());
	}
	
	
}
