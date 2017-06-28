package it.polimi.ingsw.GC_32.Client;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public interface ClientInterface extends Runnable{
	//Context Management Section
	public int getContextId();
	public void openContext(int screenId, String additionalData);
	public void openContext(JsonObject contextPayload);
	public void registerContextPayloadQueue(ConcurrentLinkedQueue<Object> queue);
	
	//Message Handling Session
	public void receiveMessage(String playerID, String message);
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue);
	public void displayMessage(String message);
	
	//Game Setup
	public void setTowerCards(int towerID, int spaceID, String name);
	public void updateTurnOrder(String[] playerIDs);
	public void setDiceValue(int blackDice, int whiteDice, int orangeDice);
	public void enableSpace(int regionID, int spaceID);
	public void disableSpace(int regionID, int spaceID);
	public void unlockZone(int playerNumber);
	
	public void registerBoard(ClientBoard board);
	public void registerPlayers(HashMap<String,ClientPlayer> playerList);
	public void registerUUID(String UUID);
	public void registerGameUUID(String UUID);
	public void registerActionRunningGameFlag(boolean flag);
	
	//Game Change
	public void moveFamiliar(int familiar, int regionID, int spaceID);
	public void moveCardToPlayer(String playerID, int regionID, int spaceID);
	public void setTrackValue(String playerID, int trackID);
	public void setCurrentPlayer(String playerID);
	public void waitTurn(boolean flag);
}