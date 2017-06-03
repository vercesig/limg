package it.polimi.ingsw.GC_32.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import it.polimi.ingsw.GC_32.Server.Game.Player;

public class SocketListener implements Runnable{

	private ServerSocket serverSocket;
	private ConcurrentHashMap<String,Socket> socketPlayerRegistry;
	
	public SocketListener(int port) throws IOException{
		this.serverSocket = new ServerSocket(port);
		System.out.println("ready!!");
		this.socketPlayerRegistry = new ConcurrentHashMap<String,Socket>();
		SocketSentinel sentinel = new SocketSentinel(this);
		Thread sentinelThread = new Thread(sentinel);
		sentinelThread.start();
	}
		
	public ConcurrentHashMap<String, Socket> getSocketPlayerRegistry(){
		return this.socketPlayerRegistry;
	}
	
	public void run(){
		while(true){
			try {
				Socket socket = serverSocket.accept();
				Player newPlayer = new Player();
				socketPlayerRegistry.put(newPlayer.getUUID(), socket);	
				
				PlayerRegistry.getInstance().registerPlayer(newPlayer.getUUID(), "SOCKET");
				System.out.println("client inserito");
				PlayerRegistry.getInstance().addPlayer(newPlayer);
			}catch(IOException e){
				break;
			}
		}		
	}	
}
