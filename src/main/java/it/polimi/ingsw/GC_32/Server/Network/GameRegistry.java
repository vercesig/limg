package it.polimi.ingsw.GC_32.Server.Network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class GameRegistry {

	private static GameRegistry instance;
	private ConcurrentHashMap<UUID, ConnectionType> playerConnectionMode;
	private ConcurrentHashMap<UUID, Player> playerTranslationTable;
	private ConcurrentHashMap<UUID, Game> gameRegistry;
	
	private GameRegistry(){
		playerConnectionMode = new ConcurrentHashMap<>();
		playerTranslationTable = new ConcurrentHashMap<>();
		gameRegistry = new ConcurrentHashMap<>();
	}
	
	public static GameRegistry getInstance(){
		if(instance==null){
			instance = new GameRegistry();
		}
		return instance;
	}
	
	public void registerPlayer(Player player, ConnectionType connectionMode){
		this.playerConnectionMode.put(player.getUUID(), connectionMode);
		this.playerTranslationTable.put(player.getUUID(), player);
	}
	
	public ConnectionType getConnectionMode(UUID playerID){
		return this.playerConnectionMode.get(playerID);
	}
	
	public ArrayList<Player> getConnectedPlayers(){
		ArrayList<Player> tmp = new ArrayList<Player>();
		playerTranslationTable.values().iterator().forEachRemaining(player -> tmp.add(player));
		return tmp;
	}
	
	public Player getPlayerFromID(UUID playerID){
		return this.playerTranslationTable.get(playerID);
	}
	
	public Game getGame(UUID gameID){
		return this.gameRegistry.get(gameID);
	}
	
	public void registerGame(Game game){
		this.gameRegistry.put(game.getUUID(), game);
	}
	
	public HashSet<UUID> getPlayerFromGameID(UUID gameID){
		HashSet<UUID> tmp = new HashSet<UUID>();
		gameRegistry.get(gameID)
		  			.getPlayerList()
		  			.iterator().forEachRemaining(player -> tmp.add(player.getUUID()));
		return tmp;
	}
	
	public List<Player> getPlayerNotInGame(){
	    ArrayList<Player> playerList = new ArrayList<>();
	    playerTranslationTable.forEach( (uuid, player) -> {
	       if(player.getGameID() == null){
	           playerList.add(player);
	       }
	    });
	    return playerList;
	}
}
