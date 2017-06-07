package it.polimi.ingsw.GC_32.Server.Network;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class GameMessageFilterTest {

	
	@Test
	public void checkFilterLockPlayerMessage() throws IOException, InterruptedException{
		Player testPlayer1 = new Player();
		testPlayer1.setPlayerName("testPlayer1");
		Player testPlayer2 = new Player();
		testPlayer2.setPlayerName("testPlayer2");
		PlayerRegistry.getInstance().addPlayer(testPlayer1);
		PlayerRegistry.getInstance().addPlayer(testPlayer2);
		
		ArrayList<Player> testPlayerList = new ArrayList<Player>();
		testPlayerList.add(testPlayer1);
		testPlayerList.add(testPlayer2);
		
		Game testGame = new Game(testPlayerList);
		testGame.setLock(testPlayer1.getUUID());
		
		
		JsonObject testMessage = new JsonObject();
		testMessage.add("MESSAGETYPE", "ASKACT");
		GameMessage gameMessagePlayer1 = new GameMessage(testPlayer1.getUUID(),testMessage.toString());
		GameMessage gameMessagePlayer2 = new GameMessage(testPlayer2.getUUID(),testMessage.toString());
		
		// inserisco i messaggi nella coda del MessageManager
		MessageManager.getInstance().putRecivedMessage(gameMessagePlayer1);
		MessageManager.getInstance().putRecivedMessage(gameMessagePlayer2);
		
		GameMessageFilter messageFilter = new GameMessageFilter(testGame);
		Thread messageFilterThread = new Thread(messageFilter);
		messageFilterThread.start();
		
		Thread.sleep(1000);
		
		messageFilter.stop();
		
		assertTrue(messageFilter.getFilteredMessage().contains(gameMessagePlayer1));
		assertFalse(messageFilter.getFilteredMessage().contains(gameMessagePlayer2));
	}
	
}