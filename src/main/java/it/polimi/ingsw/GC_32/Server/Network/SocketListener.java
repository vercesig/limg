package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Player;

/**
 * network thread responsable of opening new socket connections, which will be used by SocketReaderThread to send and recive messages
 *
 * <ul>
 *  <li> {@link #serverSocket}: the ServerSocket object used to open new connections
 *  <li> {@link #socketPlayerRegistry}: ConcurrentHashMap which map for each client UUID the relative SocketInfoContainer object
 *  <li> {@link #stop}: flag used to stop the thread
 * </ul>
 * 
 * @see SocketInfoContainer
 */
public class SocketListener implements Runnable{

	private final static Logger LOGGER = Logger.getLogger(SocketListener.class.getName());
	
	private ServerSocket serverSocket;
	private ConcurrentHashMap<UUID, SocketInfoContainer> socketPlayerRegistry;
	private Boolean stop = false;
	
	/**
	 * the constructor perform the inizialization of the SocketServer on the port indicated and socketPlayerRegistry
	 * 
	 * @param port the port used to instanziate the ServerSocket
	 * @throws IOException
	 */
	public SocketListener(int port) throws IOException{
		this.serverSocket = new ServerSocket(port);
		LOGGER.log(Level.INFO, "start");
		this.socketPlayerRegistry = new ConcurrentHashMap<>();
	}
	
	/** 
	 * allow to take the socketPlayerRegistry
	 * 
	 * @return the socketPlayerRegistry ConcurrentHashMap object
	 */
	public ConcurrentHashMap<UUID, SocketInfoContainer> getSocketPlayerRegistry(){
		return this.socketPlayerRegistry;
	}
	
	/**
	 * stop this thread
	 */
	public void kill(){
		this.stop = true;
	}
	
	/**
	 * the run method stay on listening of new connection from clients on the port used into the class constructor. When a new connection is opened a new Player is created
	 * and then registered in the GameRegistry, specifiing his connection mode. a SocketInfoContainer is createrd as well and the player is finally associated with it into
	 * the socketPlayerRegistry
	 */
	public void run(){
		LOGGER.log(Level.INFO, "launching socketsentinel");
		SocketReaderThread sentinel = new SocketReaderThread(this);
		Thread sentinelThread = new Thread(sentinel);
		sentinelThread.start();
		LOGGER.log(Level.INFO, "ready to accept connection");
		while(!stop){
			try {
				Socket socket = serverSocket.accept();
				LOGGER.log(Level.INFO, "new client connected");
				Player newPlayer = new Player();
				SocketInfoContainer newContainer = new SocketInfoContainer(socket);
				socketPlayerRegistry.put(newPlayer.getUUID(), newContainer);	
				
				GameRegistry.getInstance().registerPlayer(newPlayer, ConnectionType.SOCKET);
				
				// inoltro del CONNEST
				
				MessageManager.getInstance().sendMessge(ServerMessageFactory.buildCONNESTmessage(newPlayer.getUUID()));
				LOGGER.log(Level.INFO, "put CONNEST message in the sendQueue");
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
