package it.polimi.ingsw.GC_32.Client;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

public interface ClientInterface extends Runnable{
	//Context Management Section
	public int getScreenId();
	public void openScreen(int screenId, String additionalData);
	public void openScreen(JsonObject contextPayload);
	public void registerContextPayloadQueue(ConcurrentLinkedQueue<Object> queue);
	
	//Message Handling Session
	public void receiveMessage(String playerID, String message);
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue);
	public void displayMessage(String message);
	
	//Game Setup
	public void setTowerCards(int towerID, String[] cardArray);
	public void updateTurnOrder(String[] playerIDs);
	public void setDiceValue(int blackDice, int whiteDice, int orangeDice);
	public void enableSpace(int regionID, int spaceID);
	public void disableSpace(int regionID, int spaceID);
	
	public void registerBoard(ClientBoard board);
	public void registerPlayers(HashMap<String,ClientPlayer> playerList);
	public void registerUUID(String UUID);
	
	//Game Change
	public void moveFamiliar(int familiar, int regionID, int spaceID);
	public void moveCardToPlayer(String playerID, int regionID, int spaceID);
	public void setTrackValue(String playerID, int trackID, int value);
	public void setCurrentPlayer(String playerID);
}