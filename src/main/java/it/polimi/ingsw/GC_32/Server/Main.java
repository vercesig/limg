package it.polimi.ingsw.GC_32.Server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Server.Game.GameLobby;
import it.polimi.ingsw.GC_32.Server.Network.RMIListener;
import it.polimi.ingsw.GC_32.Server.Network.SocketListener;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

/**
 * the main method of the program. Calling the main method of the Main class will start server to execute. In particular Main class perform the Setup phase, import the
 * information from all the configuration file (load the card, the bonus tiles, the bonus relative to all the action spaces of the board, the conversion rule used 
 * for compute the final score and the bonus given by the faith track). After this main inizilize networks thread, both socket and RMI thread. 
 * When network is up, main launch GameLobby thread which will be ready to instantiate new games.
 *
 */

public class Main {
	
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
		
    public static void main( String[] args ) throws IOException, InterruptedException{
    	LOGGER.log(Level.INFO, "starting main");
		Setup setup = new Setup();
		setup.loadCard("cards.json");
		setup.loadBonusTile("bonus_tile.json");
		setup.loadBonusSpace("bonus_space.json");
		setup.loadExcommunicationTrack("excommunication_track.json");
		setup.loadConversionPoints("score_conversion.json");

		LOGGER.log(Level.INFO, "inizializing socket-side network...");
		SocketListener socketListener = new SocketListener(9500);
		Thread socketListenerThread = new Thread(socketListener);
		socketListenerThread.start();
		Thread rmiListenerThread = new Thread(new RMIListener(1099));
		rmiListenerThread.start();

		GameLobby gameLobby = new GameLobby();
		gameLobby.run();
    }
}
