package it.polimi.ingsw.GC_32.Network;

import java.io.Serializable;

public class GameMessage implements Serializable{

	private String playerID;
	private String message;
	
	public GameMessage(String playerID, String message){
		this.playerID = playerID;
		this.message = message;
	}
	
	public String getPlayerID(){
		return this.playerID;
	}
	
	public String getMessage(){
		return this.message;
	}
	
}
