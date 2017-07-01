package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;

public class RMIListener implements Runnable{
	Registry rmiRegistry;
	RMIServer server;
	HashMap<UUID, LinkedBlockingQueue<String>> rmiQueue;
	boolean stop;

	/**
	 * Initiates an RMI listening thread with a RMI registry on the specified port
	 * @param port the port to open the RMI registry on
	 * @throws IOException
	 */
	public RMIListener(int port) throws IOException{
		this.rmiRegistry = LocateRegistry.createRegistry(port);
		this.rmiQueue = new HashMap<>();
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
			if(MessageManager.getInstance().getRMISendQueue().size() > 0){
				GameMessage message = MessageManager.getInstance().getRMISendQueue().poll();
				if(message != null && this.rmiQueue.get(message.getPlayerUUID())!=null){
						this.rmiQueue.get(message.getPlayerUUID()).add(message.toJson().toString());
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