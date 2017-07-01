package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Server.Game.Player;

public class RMIListener implements Runnable{
	Registry rmiRegistry;
	RMIServer server;
	ConcurrentHashMap<UUID, LinkedBlockingQueue<String>> rmiQueue;
	boolean stop;

	/**
	 * Initiates an RMI listening thread with a RMI registry on the specified port
	 * @param port the port to open the RMI registry on
	 * @throws IOException
	 */
	public RMIListener(int port) throws IOException{
		this.rmiRegistry = LocateRegistry.createRegistry(port);
		this.rmiQueue = new ConcurrentHashMap<>();
		this.server = new RMIServer(this.rmiQueue);
		this.stop = false;
		try{
			this.rmiRegistry.bind("LIMG_Conn", this.server);
		} catch(AlreadyBoundException e){
			throw new IOException(e);
		}
	}

	@Override
	public void run() {
		while(!stop){
			if(!MessageManager.getInstance().getRMISendQueue().isEmpty()){
				GameMessage message = MessageManager.getInstance().getRMISendQueue().poll();
				if(message != null){
				    sendMessage(message);
				}
			}
		}
	}
	
	private void sendMessage(GameMessage message){
	    if(!message.isBroadcast() && this.rmiQueue.get(message.getPlayerUUID()) != null){
	        this.rmiQueue.get(message.getPlayerUUID()).add(message.toJson().toString());
	    } else {
	        for(Player player : GameRegistry.getInstance().getGame(message.getGameID())
	                                                      .getPlayerList()){
	            if(GameRegistry.getInstance().getConnectionMode(player.getUUID()) == ConnectionType.RMI){
	                this.rmiQueue.get(player.getUUID()).add(message.toJson().toString());
	            }
	        }
	    }
	}

	/**
	 * Tells the thread to stop gracefully
	 */
	public void kill(){
		this.stop = true;
	}
}