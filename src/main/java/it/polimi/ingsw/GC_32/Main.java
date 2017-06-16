package it.polimi.ingsw.GC_32;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Server.Game.GameLobby;
import it.polimi.ingsw.GC_32.Server.Network.SocketListener;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class Main {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
    public static void main( String[] args ) throws IOException, InterruptedException{
            	    	
    	LOGGER.log(Level.INFO, "starting main");
		Setup setup = new Setup();
		setup.loadCard();
    	
		LOGGER.log(Level.INFO, "inizializing socket-side network...");
		SocketListener socketListener = new SocketListener(9500);
		Thread socketListenerThread = new Thread(socketListener);
		socketListenerThread.start();
		
		GameLobby gameLobby = new GameLobby();		
    }
}
