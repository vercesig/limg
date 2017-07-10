package it.polimi.ingsw.GC_32.Server.Network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Server.Game.Game;
import it.polimi.ingsw.GC_32.Server.Game.Player;

/**
 * singleton class which contains all the information about all the games handle by the server. It allows to register games, register players to a specific game, get
 * all the players connected to a specific game, get a Player object from its UUID and so on
 *
 * <ul>
 * <li>{@link #gameRegistry}; HashMap which map each Game with its UUID</li>
 * <li>{@link #playerTranslationTable}: HashMap which map each Player with its UUID</li>
 * <li>{@link #playerConnectionMode}: HashMap which map each player with its connection mode</li>
 * <li>{@link #instance}; the instance of this singleton</li>
 * </ul>
 *
 * @see Players, ConnectionType, Game
 */
public class GameRegistry {

	private static GameRegistry instance;
	private ConcurrentHashMap<UUID, ConnectionType> playerConnectionMode;
	private ConcurrentHashMap<UUID, Player> playerTranslationTable;
	private ConcurrentHashMap<UUID, Game> gameRegistry;
	private ConcurrentLinkedQueue<Player> newPlayers;
	private int newPlayersCount;
	
	/**
	 * initialize the GameRegistry
	 */
	private GameRegistry(){
		playerConnectionMode = new ConcurrentHashMap<>();
		playerTranslationTable = new ConcurrentHashMap<>();
		gameRegistry = new ConcurrentHashMap<>();
		newPlayers = new ConcurrentLinkedQueue<Player>();
		newPlayersCount = 0;
	}
	
	/**
	 * allows to retrive the instance of the singleton class
	 * @return the instance of this class
	 */
	public static GameRegistry getInstance(){
		if(instance==null){
			instance = new GameRegistry();
		}
		return instance;
	}
	
	/**
	 * register player in the list of connected player, specifing the connection mode the player has used to connect
	 * 
	 * @param player player that must be registered
	 * @param connectionMode network mode used by the player to play (socket or RMI)
	 */
	public void registerPlayer(Player player, ConnectionType connectionMode){
		this.playerConnectionMode.put(player.getUUID(), connectionMode);
		this.playerTranslationTable.put(player.getUUID(), player);
		this.newPlayers.offer(player);
		incrNewPlayers();
	}
	
	/**
	 * return the connection mode giveb the UUID of the player
	 * @param playerID the UUID of the player of which the connection mode must be returned
	 * @return the connection mode used by the player
	 */
	public ConnectionType getConnectionMode(UUID playerID){
		return this.playerConnectionMode.get(playerID);
	}
	
	/**
	 * return the list of all the players connected
	 * @return an ArrayList containing all the players connected
	 */
	public ArrayList<Player> getConnectedPlayers(){
		ArrayList<Player> tmp = new ArrayList<Player>();
		playerTranslationTable.values().iterator().forEachRemaining(player -> tmp.add(player));
		return tmp;
	}
	
	/**
	 * given a specific UUID, return the player object associated whit it
	 * @param playerID the player UUID
	 * @return the player object associacted to the UUID passed as parameter
	 */
	public Player getPlayerFromID(UUID playerID){
		return this.playerTranslationTable.get(playerID);
	}
	
	/**
	 * given a specific UUID, return the game object associated whit it
	 * @param gameID the UUID of the game 
	 * @return the Game object associated to the UUID passed as parameter
	 */
	public Game getGame(UUID gameID){
		return this.gameRegistry.get(gameID);
	}
	
	/**
	 * register a game object into the game registry
	 */
	public void registerGame(Game game){
		this.gameRegistry.put(game.getUUID(), game);
	}
	
	/**
	 * given the UUID of a Game object, returns all the players associated with it
	 * @param gameID the UUID of the game
	 * @return the set of player UUID associated with the given game
	 */
	public HashSet<UUID> getPlayerFromGameID(UUID gameID){
		HashSet<UUID> tmp = new HashSet<>();
		gameRegistry.get(gameID)
		  			.getPlayerList()
		  			.iterator().forEachRemaining(player -> tmp.add(player.getUUID()));
		return tmp;
	}
	
	/**
	 * return the list of player that are not connected to a game
	 * @return a player list containing players who are not connected to a game
	 */
	public List<Player> getPlayersNotInGame(){
	    ArrayList<Player> tmpList = new ArrayList<>();
	    tmpList.addAll(this.newPlayers);
	    return tmpList;
	}
	
	/**
	 * Tells how many players are not in a game
	 */
	public int queuedPlayersCount(){
	    return this.newPlayersCount;
	}
	
	/**
	 * Returns at most maxPlayers that are not in a game
	 * @param maxPlayers maximum number of players to return
	 * @return
	 */
    public List<Player> getNewPlayers(int maxPlayers) {
        ArrayList<Player> tmpList = new ArrayList<>();
        for(int i = 0; i < maxPlayers; i++){
            Player player = this.newPlayers.poll();
            if(player != null){
                tmpList.add(player);
                decrNewPlayers();
            } else {
                break;
            }
        }
        return tmpList;
    }
    
    private synchronized void incrNewPlayers(){
        this.newPlayersCount++;
    }
    
    private synchronized void decrNewPlayers(){
        this.newPlayersCount--;
    }
}
