package it.polimi.ingsw.GC_32.Client;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Client.CLI.Context;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

/**
 * interface describing the functionaliy offered by the CLI and GUI thread.
 */

public interface ClientInterface extends Runnable{
	
	/**
	 * open a context using the information specified into the JsonObject message passed as argument
	 * @param contextMessage the received CONTEXT message used to customize the context 
	 */
	public void openContext(JsonObject contextPayload);
	
	//Message Handling Session
	
	/**
	 * display the chat message which has been just received
	 * @param playerID the name of the player who has sent the message
	 * @param message the sent message
	 */
	public void receiveMessage(String playerID, String message);
	
	/**
	 * register the send message queue
	 * @param the queue which must be registered
	 */
	public void registerSendMessageQueue(ConcurrentLinkedQueue<String> queue);
	
	/**
	 * display the given message
	 * @param message the message to be displayed
	 */
	public void displayMessage(String message);
	
	/**
	 * register the board
	 * @param board the board which must be registered
	 */
	public void registerBoard(ClientBoard board);
	
	/**
	 * register the player list
	 * @param HashMap containing all the information about the player connected to the game
	 */
	public void registerPlayers(HashMap<String,ClientPlayer> playerList);
	
	/**
	 * register the player UUID
	 * @param the UUID of the player
	 */
	public void registerPlayerUUID(String UUID);
	
	/**
	 * register the UUID of the game to which the client has been connected
	 * @param the game UUID
	 */
	public void registerGameUUID(String UUID);
	
	/**
	 * set false the leaderStartPhase flag. This tells that the leader card distribuition phase is finished
	 */
	public void leaderStartPhaseEnd();
	
	/**
	 * allows to get all the available contexts
	 * @return an array of all available contexts
	 */
	public Context[] getContextList();
	
	/**
	 * change the value of the idleRunFlag, to stop or reactivate the ZeroLevelContextThread
	 * @param idleRunFlag the value of the idleRun flag
	 */
	public void setIdleRun(boolean idleRunFlag);
	
	/**
	 * change the value to the wait boolean flag, to tell the client if is his turn or not
	 * @param flag the value of the wait flag
	 */
	public void waitTurn(boolean flag);
}