package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;
import it.polimi.ingsw.GC_32.Common.Utils.Utils;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;

public class GameLobby implements KillableRunnable{
	
	private final static Logger LOGGER = Logger.getLogger(GameLobby.class.getName());
	
	private int MIN_PLAYERS = 2;
	private int MAX_PLAYERS = 4;
	private int startGameTimeout = 1000;
	private Game game;
	private boolean stop;
	
	public GameLobby() throws IOException{
	    this.stop = false;
	}

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "GameLobby start, waiting for players");
        while(!stop){
            if(GameRegistry.getInstance().queuedPlayersCount() >= MIN_PLAYERS){
                LOGGER.log(Level.INFO, "minimum number of players (%s) achieved.", MIN_PLAYERS);
                LOGGER.log(Level.INFO, "timeout started, waiting for other players");
                long startTimeoutTime = System.currentTimeMillis();
                while(true){
                    if(startTimeoutTime + startGameTimeout < System.currentTimeMillis()){
                        LOGGER.log(Level.INFO, "timeout finished. starting new game...");
                        startGame();
                        break;
                    }
                    if(GameRegistry.getInstance().queuedPlayersCount() >= MAX_PLAYERS){
                        LOGGER.log(Level.INFO, "maximum number of players (%s) achieved. timeout stopped", MAX_PLAYERS);
                        startGame();
                        break;
                    }
                    Utils.safeSleep(200);
                }
            }
        }
    }

    @Override
    public void kill() {
        stop = true;
    }
    
    public void startGame(){
        UUID newGameId = UUID.randomUUID();
        game = new Game(GameRegistry.getInstance().getNewPlayers(MAX_PLAYERS), newGameId);
        GameRegistry.getInstance().registerGame(game);
        LOGGER.log(Level.INFO, "new game created with %s players", game.getPlayerList().size());
        LOGGER.log(Level.INFO, "launching game thread");
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}
