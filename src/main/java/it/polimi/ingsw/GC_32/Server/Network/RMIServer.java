package it.polimi.ingsw.GC_32.Server.Network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import it.polimi.ingsw.GC_32.Common.Network.ConnectionType;
import it.polimi.ingsw.GC_32.Common.Network.GameMessage;
import it.polimi.ingsw.GC_32.Common.Network.RMIConnection;
import it.polimi.ingsw.GC_32.Server.Game.Player;

/**
 * perform all the function linked to the RMI connections management. In particular open, close the
 * RMI connection with the client and allow to send and recive messages
 * 
 * <ul>
 * 	<li>{@link #rmiQueue}: ConcurrentHashMap which associate each player connected thoroght RMI to a queue of messages used by the server to send message
 * to the client</li>
 * </ul>
 * 
 * @see RMIConnection
 *
 */
public class RMIServer implements RMIConnection{
	private ConcurrentHashMap<UUID, LinkedBlockingQueue<String>> rmiQueue;

	/**
	 * the constructor setup the server to accept RMI connections
	 * 
	 * @param queue the queue into which the clients will be registered
	 * @throws RemoteException
	 */
	public RMIServer(ConcurrentHashMap<UUID, LinkedBlockingQueue<String>> queue) throws RemoteException{
		UnicastRemoteObject.exportObject(this, 0);
		this.rmiQueue = queue;
	}

	/**
	 * open the RMI connection with a player. when a new client connects with the server call this method which will instantiate
	 * a new player object and will register it into the GameRegistry, specifing the connection mode. Finally open() associate the player (with his UUID) to the corresponing
	 * message queue. A CONNEST message is sent as well.
	 * 
	 * @throws RemoteException
	 */
	@Override
	public UUID open() throws RemoteException{
		Player newPlayer = new Player();
		GameRegistry.getInstance().registerPlayer(newPlayer, ConnectionType.RMI);
		rmiQueue.put(newPlayer.getUUID(), new LinkedBlockingQueue<>());
		rmiQueue.get(newPlayer.getUUID()).add((ServerMessageFactory.buildCONNESTmessage(newPlayer.getUUID()).toJson().toString()));
		return newPlayer.getUUID();
	}

	/**
	 * close the TMI connection with this player
	 * 
	 * @param id the UUID of the player which has closed the connection
	 * @throws RemoteException
	 */
	@Override
	public void close(UUID id) throws RemoteException{
		rmiQueue.remove(id);
	}

	/**
	 * allow to send message to the server. when the client wants send a message, he will simply call this method which will build a GameMessage and finally will 
	 * insert it into the MessageManager queue
	 * 
	 * @param id the UUID of the client who has sent the message
	 * @param message the message to send
	 * @throws RemoteException
	 */
	@Override
	public void sendMessage(UUID id, String message) throws RemoteException{
		GameMessage gMessage = new GameMessage(message, id);
		MessageManager.getInstance().putRecivedMessage(gMessage);
	}

	/**
	 * allow to get message from the server, looking into the relative message queue retrived by rmiQueue
	 * 
	 * @param id the UUID of the player who wants get messages
	 * @throws RemoteException
	 */
	@Override
	public String getMessage(UUID id) throws RemoteException{
		try {
			return rmiQueue.get(id).take();
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		}
		return null;
	}

	/**
	 * allow the client to know if there are messages available for him.
	 * 
	 * @param id the UUID of the player who wants get messages
	 * @throws RemoteException
	 * @return true if the message queue has some message for the client, false if not
	 */
	@Override
	public boolean hadMessage(UUID id) throws RemoteException{
		return (!rmiQueue.get(id).isEmpty());
	}
}