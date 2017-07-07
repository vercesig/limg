package it.polimi.ingsw.GC_32.Client;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public interface ClientInterface extends Runnable{
	//Context Management Section
	public void openContext(JsonObject contextPayload);
	
	//Message Handling Session
	public void receiveMessage(String playerID, String message);
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue);
	public void displayMessage(String message);
	
	
	public void registerBoard(ClientBoard board);
	public void registerPlayers(HashMap<String,ClientPlayer> playerList);
	public void registerPlayerUUID(String UUID);
	public void registerGameUUID(String UUID);
	
	public void leaderStartPhaseEnd();
	
	//Game Change
	public void waitTurn(boolean flag);
	public void update();
}