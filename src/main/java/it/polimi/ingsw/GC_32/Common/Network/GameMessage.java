package it.polimi.ingsw.GC_32.Common.Network;

import java.util.UUID;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class GameMessage{
	private UUID gameID;
	private UUID playerID;
	private JsonValue message;
	private String opcode;
	private boolean broadcast;
	
	public GameMessage(UUID gameID, UUID playerID, String opcode, JsonValue message){
		this.gameID = gameID;
		this.playerID = playerID;
		this.message = message;
		this.opcode = opcode;
		this.broadcast = false;
	}
	
	public UUID getGameID(){
		return this.gameID;
	}
	
	public String getPlayerID(){
		return this.playerID.toString();
	}
	
	public UUID getPlayerUUID(){
		return this.playerID;
	}
	
	public JsonValue getMessage(){
		return this.message;
	}
	
	public String getOpcode(){
		return this.opcode;
	}
	
	public JsonValue toJson(){
		JsonObject json = new JsonObject();
		if(gameID != null){
			json.add("GameID", gameID.toString());
		} else {
			json.add("GameID", "");
		}
		json.add("PlayerID", getPlayerID());
		json.add("MESSAGETYPE", opcode);
		json.add("PAYLOAD", message.toString());
		return json;
	}
	
	public boolean isBroadcast(){
		return this.broadcast;
	}
	
	public void setBroadcast(){
		this.broadcast = true;
	}
}
