package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class SocketListener implements Runnable{

	private final static Logger LOGGER = Logger.getLogger(SocketListener.class.getName());
	
	private ServerSocket serverSocket;
	private ConcurrentHashMap<UUID, SocketInfoContainer> socketPlayerRegistry;
	private Boolean stop = false;
	
	public SocketListener(int port) throws IOException{
		this.serverSocket = new ServerSocket(port);
		LOGGER.log(Level.INFO, "start");
		this.socketPlayerRegistry = new ConcurrentHashMap<>();
	}
		
	public ConcurrentHashMap<UUID, SocketInfoContainer> getSocketPlayerRegistry(){
		return this.socketPlayerRegistry;
	}
	
	public void kill(){
		this.stop = true;
	}
	
	public void run(){
		LOGGER.log(Level.INFO, "launching socketsentinel");
		SocketReaderThread sentinel = new SocketReaderThread(this);
		Thread sentinelThread = new Thread(sentinel);
		sentinelThread.start();
		LOGGER.log(Level.INFO, "ready to accept connection");
		while(true){
			try {
				Socket socket = serverSocket.accept();
				LOGGER.log(Level.INFO, "new client connected");
				Player newPlayer = new Player();
				SocketInfoContainer newContainer = new SocketInfoContainer(socket);
				socketPlayerRegistry.put(newPlayer.getUUID(), newContainer);	
				
				GameRegistry.getInstance().registerPlayer(newPlayer, ConnectionType.SOCKET);
				
				// inoltro del CONNEST
				JsonObject CONNEST = new JsonObject();
				CONNEST.add("PLAYERID", newPlayer.getID());
				GameMessage CONNESTmessage = new GameMessage(null, newPlayer.getUUID(),"CONNEST", CONNEST);
				MessageManager.getInstance().sendMessge(CONNESTmessage);
				LOGGER.log(Level.INFO, "put CONNEST message in the sendQueue");
				
				GameRegistry.getInstance().registerPlayer(newPlayer, ConnectionType.SOCKET);
			}catch(IOException e){
				Logger.getLogger("").log(Level.SEVERE, "context", e);
				break;
			}
			if(this.stop){
				break;
			}
		}		
	}	
}
