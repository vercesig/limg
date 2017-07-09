package it.polimi.ingsw.GC_32.Common.Network;

import java.util.UUID;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * GameMessage is the way the server represent interally the messages which must be sent to the client or which are received from the client. A GameMessage contains not 
 * only the message sent by the client but the UUID of the player who sent the message, the game UUID of the message, the type of message sent, and a flag which tells
 * if the message must be sent in broadcast or not.
 * 
 * <ul>
 * <li>{@link #broadcast}: if set true, the message must be sent to all the client connected to the game</li>
 * <li>{@link #gameID}: the UUID of the game</li>
 * <li>{@link #message}: the JsonValue representing the message sent by the client</li>
 * <li>{@link #opcode}: string representing the type of message received</li>
 * <li>{@link #playerID}: the UUID of the player who has sent the message</li>
 * </ul>
 * 
 * note that GameMessages are never sent throught the network. Server and client only communicates by string.
 */
public class GameMessage{
	private UUID gameID;
	private UUID playerID;
	private JsonValue message;
	private String opcode;
	private boolean broadcast;
	
	/**
	 * create a new GameMessage
	 * @param gameID the UUID of the game
	 * @param playerID the UUID of the player who has sent the message or to whom the message must be sent
	 * @param opcode the type of message the client has sent
	 * @param message the message, represented as JsonValue
	 */
	public GameMessage(UUID gameID, UUID playerID, String opcode, JsonValue message){
		this.gameID = gameID;
		this.playerID = playerID;
		this.message = message;
		this.opcode = opcode;
		this.broadcast = false;
	}
	
	/**
	 * create new GameMessage from a string
	 * @param packet the string representation of the message
	 * @param playerID the UUID of the player who has sent the message or to whom the messsage must be sent
	 */
	public GameMessage(String packet, UUID playerID){
		JsonObject parsedMessage = Json.parse(packet).asObject();						
		this.gameID = UUID.fromString(parsedMessage.get("GameID").asString());
		this.playerID = playerID;
		this.opcode = parsedMessage.get("MESSAGETYPE").asString();
		this.message = parsedMessage.get("PAYLOAD");
		this.broadcast = false;
	}
	
	/**
	 * get the game message UUID
	 * @return the UUID of the game
	 */
	public UUID getGameID(){
		return this.gameID;
	}
	
	/**
	 * get the player UUID of the message
	 * @return the UUID of the player, returned as String
	 */
	public String getPlayerID(){
		return this.playerID.toString();
	}
	
	/**
	 * get the UUID of the message
	 * @return the UUID of the player
	 */
	public UUID getPlayerUUID(){
		return this.playerID;
	}
	
	/**
	 * get the message content into the game message
	 * @return the JsonValue representing the message that was effectively sent by the client
	 */
	public JsonValue getMessage(){
		return this.message;
	}
	
	/**
	 * get the opcode of the message
	 * @return the type of message which was sent
	 */
	public String getOpcode(){
		return this.opcode;
	}
	
	/**
	 * given a string representing the message the server want send, this method build the equivalent JSON representation of that message, in order to allow the client
	 * to parse it in a very simply way
	 * @return the JsonValue representation of the message
	 */
	public JsonValue toJson(){
		JsonObject json = new JsonObject();
		if(gameID != null){
			json.add("GameID", gameID.toString());
		} else {
			json.add("GameID", "");
		}
		if(!broadcast)
			json.add("PlayerID", getPlayerID());
		json.add("MESSAGETYPE", opcode);
		json.add("PAYLOAD", message.toString());
		return json;
	}
	
	/**
	 * tells to the network thread if the message must be sent in broadcast
	 * @return if true, the message must be sent in broadcast. otherwise not
	 */
	public boolean isBroadcast(){
		return this.broadcast;
	}
	
	/**
	 * set the broadcast flag to true, this means the message will be sent in broadcast
	 */
	public void setBroadcast(){
		this.broadcast = true;
	}
}
