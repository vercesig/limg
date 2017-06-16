package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;
import it.polimi.ingsw.GC_32.Common.Game.ResourceSet;
import it.polimi.ingsw.GC_32.Common.Network.ClientMessageFactory;

public class NetworkClient{

	private MsgConnection network;
	//private SlimPlayer I;
	private HashMap<String,ClientPlayer> players;
	private ClientBoard clientBoard;
	
	
	public NetworkClient(){
		network = new SocketMsgConnection();
		players = new HashMap<String, ClientPlayer>();
	}
	
	public MsgConnection getConnection(){
		return this.network;
	}
	
	public ClientBoard getClientBoard(){
		return this.clientBoard;
	}
	
	public void setClientBoard(ClientBoard board){
		this.clientBoard = board;
	}
	
	public HashMap<String, ClientPlayer> getPlayers(){
		return this.players;
	} 
}
