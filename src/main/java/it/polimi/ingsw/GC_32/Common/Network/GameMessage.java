package it.polimi.ingsw.GC_32.Common.Network;

import java.io.Serializable;

public class GameMessage implements Serializable{

	private String playerID;
	private String message;
	private String opcode;
	
	public GameMessage(String playerID, String opcode, String message){
		this.playerID = playerID;
		this.message = message;
		this.opcode = opcode;
	}
	
	public String getPlayerID(){
		return this.playerID;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String getOpcode(){
		return this.opcode;
	}
	
}
