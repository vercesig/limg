package it.polimi.ingsw.GC_32;

import java.io.IOException;

import it.polimi.ingsw.GC_32.Server.Game.GameLobby;
import it.polimi.ingsw.GC_32.Server.Network.SocketListener;
import it.polimi.ingsw.GC_32.Server.Setup.Setup;

public class Main {
		
    public static void main( String[] args ) throws IOException, InterruptedException{
        
    	System.out.println("[MAIN] starting Main...");
		Setup setup = new Setup();
		setup.loadCard();
    	
    	System.out.println("[MAIN] inizializing socket-side newtork...");
		SocketListener socketListener = new SocketListener(9500);
		Thread socketListenerThread = new Thread(socketListener);
		socketListenerThread.start();
		
		GameLobby gameLobby = new GameLobby();		
    }
}
