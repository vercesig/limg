package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import com.eclipsesource.json.JsonObject;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Utils.Logger;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class SocketListener implements Runnable{

	private ServerSocket serverSocket;
	private ConcurrentHashMap<String,SocketInfoContainer> socketPlayerRegistry;
	private Boolean stop = false;
	
	public SocketListener(int port) throws IOException{
		this.serverSocket = new ServerSocket(port);
		System.out.println("[SOCKETLISTENER] start");
		this.socketPlayerRegistry = new ConcurrentHashMap<String,SocketInfoContainer>();
	}
		
	public ConcurrentHashMap<String, SocketInfoContainer> getSocketPlayerRegistry(){
		return this.socketPlayerRegistry;
	}
	
	public void kill(){
		this.stop = true;
	}
	
	public void run(){
		System.out.println("[SOCKETLISTENER] launching socketsentinel");
		SocketSentinel sentinel = new SocketSentinel(this);
		Thread sentinelThread = new Thread(sentinel);
		sentinelThread.start();
		System.out.println("[SOCKETLISTENER] ready to accept connection");
		while(true){
			try {
				Socket socket = serverSocket.accept();
				System.out.println("[SOCKETLISTENER] new client connected");
				Player newPlayer = new Player();
				SocketInfoContainer newContainer = new SocketInfoContainer(socket);
				socketPlayerRegistry.put(newPlayer.getUUID(), newContainer);	
				
				PlayerRegistry.getInstance().registerPlayer(newPlayer.getUUID(), ConnectionType.SOCKET);
				
				// inoltro del CONNEST
				JsonObject CONNEST = new JsonObject();
				CONNEST.add("PLAYERID", newPlayer.getUUID());
				GameMessage CONNESTmessage = new GameMessage(newPlayer.getUUID(),"CONNEST", CONNEST.toString());
				MessageManager.getInstance().sendMessge(CONNESTmessage);
				System.out.println("[SOCKETLISTENER] put CONNEST message in the sendQueue");
				
				PlayerRegistry.getInstance().addPlayer(newPlayer);
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
