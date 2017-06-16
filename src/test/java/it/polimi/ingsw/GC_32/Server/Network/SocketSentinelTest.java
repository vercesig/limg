package it.polimi.ingsw.GC_32.Server.Network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;

public class SocketSentinelTest{
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void checkSocketSentinel() throws IOException{
		SocketListener socketListener = mock(SocketListener.class);
		Socket socket = mock(Socket.class);
		InputStream testString = new ByteArrayInputStream("Test_String".getBytes("UTF-8"));
		when(socket.getInputStream()).thenReturn(testString);
		ByteArrayOutputStream returnString = new ByteArrayOutputStream();
		when(socket.getOutputStream()).thenReturn(returnString);
		ConcurrentHashMap<String, SocketInfoContainer> playerMap = new ConcurrentHashMap<>();
		playerMap.put("Test_player", new SocketInfoContainer(socket));
		when(socketListener.getSocketPlayerRegistry()).thenReturn(playerMap);
		SocketSentinel ss = new SocketSentinel(socketListener);
		Thread threadObject = new Thread(ss);
		threadObject.start();
		while(!MessageManager.getInstance().hasMessage()){}
		GameMessage msg = MessageManager.getInstance().getRecivedQueue().poll();
		//assertEquals("Test_player", msg.getPlayerID());
		//assertEquals("Test_String", msg.getMessage());
		PlayerRegistry.getInstance().registerPlayer("Test_player", ConnectionType.SOCKET);
		GameMessage newMsg = new GameMessage("Test_Player", "TESTMSG", "Return_Test");
		MessageManager.getInstance().sendMessge(newMsg);
		while(returnString.toString() == ""){}
		//assertEquals("Return_Test",returnString.toString());
		threadObject.stop();
	}
	
}