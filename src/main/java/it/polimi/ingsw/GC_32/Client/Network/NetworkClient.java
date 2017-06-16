package it.polimi.ingsw.GC_32.Client.Network;

import java.util.HashMap;
import it.polimi.ingsw.GC_32.Client.Game.ClientBoard;
import it.polimi.ingsw.GC_32.Client.Game.ClientPlayer;

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
