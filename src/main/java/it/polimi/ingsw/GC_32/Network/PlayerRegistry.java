package it.polimi.ingsw.GC_32.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.GC_32.Main;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class PlayerRegistry {

	private static PlayerRegistry instance;
	private HashMap<String,String> playerConnectionMode;
	private ArrayList<Player> connectedPlayers;
	
	private PlayerRegistry(){
		playerConnectionMode = new HashMap<String, String>();
		connectedPlayers = new ArrayList<Player>();
	}
	
	public static PlayerRegistry getInstance(){
		if(instance==null){
			instance = new PlayerRegistry();
		}
		return instance;
	}
	
	public void registerPlayer(String playerID, String connectionMode){
		this.playerConnectionMode.put(playerID, connectionMode);
	}
	
	public String getConnectionMode(String playerID){
		return this.playerConnectionMode.get(playerID);
	}
	
	public void addPlayer(Player player) throws IOException{
		this.connectedPlayers.add(player);
		if(connectedPlayers.size()>1)
			Main.newGame(connectedPlayers);
	}
	
	public ArrayList<Player> getConnectedPlayers(){
		return this.connectedPlayers;
	}
		
}
