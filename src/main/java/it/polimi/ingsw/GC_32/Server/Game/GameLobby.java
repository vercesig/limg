package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;

public class GameLobby {
	
	private final static Logger LOGGER = Logger.getLogger(GameLobby.class.getName());
	
	private int MIN_PLAYERS = 2;
	private int MAX_PLAYERS = 4;
	private int startGameTimeout = 1000;
	private Game game;
	
	public GameLobby() throws InterruptedException, IOException{
		LOGGER.log(Level.INFO, "GameLobby start, waiting for players");
		while(true){
			if(GameRegistry.getInstance().getConnectedPlayers().size()>=MIN_PLAYERS){
				LOGGER.log(Level.INFO, "minimum number of players ("+MIN_PLAYERS+") achieved.");
				LOGGER.log(Level.INFO, "timeout started, waiting for other players");
				long startTimeoutTime = System.currentTimeMillis();
				while(true){
					if(startTimeoutTime + startGameTimeout < System.currentTimeMillis()){
						LOGGER.log(Level.INFO, "timeout finished. starting new game...");
						break;
					}
					if(GameRegistry.getInstance().getConnectedPlayers().size()==MAX_PLAYERS){
						LOGGER.log(Level.INFO, "maximum number of players ("+MAX_PLAYERS+") achieved. timeout stopped");
						break;
					}
					Thread.sleep(200);
				}
				break;
			}
		}
		UUID newGameId = UUID.randomUUID();
		game = new Game(GameRegistry.getInstance().getConnectedPlayers(), newGameId);
		GameRegistry.getInstance().registerGame(game);
		LOGGER.log(Level.INFO, "new game created with "+game.getPlayerList().size()+" players");
		LOGGER.log(Level.INFO, "launching game thread");
		Thread gameThread = new Thread(game);
		gameThread.start();
	}
}
