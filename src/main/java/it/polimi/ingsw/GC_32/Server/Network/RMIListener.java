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

/**
* network thread responsable of the management of RMI connections. After RMIServer has opened a RMI connection, RMIListener perform the sending of 
 * messages to the clients on a RMI connection
 * 
 * <ul>
 * <li>{@link #rmiQueue}: : ConcurrentHashMap which associate each player connected thoroght RMI to a queue of messages used by the server to send message</li>
 * <li>{@link #rmiRegistry}: the RMI registry</li>
 * <li>{@link #server}: the RMI server</li>
 * <li>{@link #stop}: flag used to stop this thread</li>
 * </ul>
 * 
 *  @see RMIServer
 *
 */
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

	/**
	 * run method handels the sending of messages by the server putting messages into the corresponding client sendQueue (taken from the rmiQueue HashMap of this class)
	 */
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
	
	/**
	 * send the message to the client, putting the message in the corresponding client sendQueue, looking to the UUID of the player inside the GameMessage object
	 * sendMessage() method is called by run()
	 * 
	 * @see GameMessage
	 * @param message the GameMessage to send
	 */
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