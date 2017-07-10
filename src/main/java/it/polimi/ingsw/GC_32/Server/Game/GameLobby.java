package it.polimi.ingsw.GC_32.Server.Game;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;
import it.polimi.ingsw.GC_32.Common.Utils.Utils;
import it.polimi.ingsw.GC_32.Server.Network.GameRegistry;

/**
 * thread responsable of starting new games when the maximum number of players has been achieved or the start game timeout is finished
 *
 *<ul>
 *<li>{@link #game}: the game which must be registered into the GameRegistry and then started</li>
 *<li>{@link GameLobby#MAX_PLAYERS}: the maximum number of game players</li>
 *<li>{@link #MIN_PLAYERS}: the minimum number of game players</li>
 *<li>{@link #startGameTimeout}: the timeout beyond which the game is started, indipendently on the number of players connected (the minimum number of players 
 * must have been achieved)</li>
 *<li>{@link #stop}: used to stop this thread</li>
 *</ul>
 *
 * @see Game
 */

public class GameLobby implements KillableRunnable{
	
	private final static Logger LOGGER = Logger.getLogger(GameLobby.class.getName());
	
	private int MIN_PLAYERS = 2;
	private int MAX_PLAYERS = GameConfig.getInstance().getConfig().getSecondArg();
	private long startGameTimeout = GameConfig.getInstance().getConfig().getFirstArg();
	private Game game;
	private boolean stop;
	
	/**
	 * initialize the GameLobby
	 * @throws IOException
	 */
	public GameLobby() throws IOException{
	    this.stop = false;
	}

	/**
	 * run method wait for new players that are just connected and then start a new game when the right condition are satisfied.
	 */
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
            Utils.safeSleep(200);
        }
    }

    /**
     * stop this thread
     */
    @Override
    public void kill() {
        stop = true;
    }
    
    /**
     * create a new game, associating to it a UUID and registering it into the GameRegistry. finally the new Game thread is launched and the match can start
     */
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
