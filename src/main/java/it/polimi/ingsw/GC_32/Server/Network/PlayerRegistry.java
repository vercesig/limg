package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.GC_32.Main;
import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class PlayerRegistry {

	private static PlayerRegistry instance;
	private HashMap<String,ConnectionType> playerConnectionMode;
	private HashMap<String,Player> connectedPlayers;
	
	private PlayerRegistry(){
		playerConnectionMode = new HashMap<String, ConnectionType>();
		connectedPlayers = new HashMap<String,Player>();
	}
	
	public static PlayerRegistry getInstance(){
		if(instance==null){
			instance = new PlayerRegistry();
		}
		return instance;
	}
	
	public void registerPlayer(String playerID, ConnectionType connectionMode){
		this.playerConnectionMode.put(playerID, connectionMode);
	}
	
	public ConnectionType getConnectionMode(String playerID){
		return this.playerConnectionMode.get(playerID);
	}
	
	public void addPlayer(Player player) throws IOException{
		this.connectedPlayers.put(player.getUUID(),player);
		if(connectedPlayers.size()>1)
			Main.newGame(getConnectedPlayers());
	}
	
	public ArrayList<Player> getConnectedPlayers(){
		ArrayList<Player> tmp = new ArrayList<Player>();
		connectedPlayers.values().iterator().forEachRemaining(player -> {
			tmp.add(player);
		});
		return tmp;
	}
	
	public Player getPlayerFromID(String playerID){
		return this.connectedPlayers.get(playerID);
	}
		
}
