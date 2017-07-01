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

public class RMIServer implements RMIConnection{
	private ConcurrentHashMap<UUID, LinkedBlockingQueue<String>> rmiQueue;

	public RMIServer(ConcurrentHashMap<UUID, LinkedBlockingQueue<String>> queue) throws RemoteException{
		UnicastRemoteObject.exportObject(this, 0);
		this.rmiQueue = queue;
	}

	@Override
	public UUID open() throws RemoteException{
		Player newPlayer = new Player();
		GameRegistry.getInstance().registerPlayer(newPlayer, ConnectionType.RMI);
		rmiQueue.put(newPlayer.getUUID(), new LinkedBlockingQueue<>());
		rmiQueue.get(newPlayer.getUUID()).add((ServerMessageFactory.buildCONNESTmessage(newPlayer.getUUID()).toJson().toString()));
		return newPlayer.getUUID();
	}

	@Override
	public void close(UUID id) throws RemoteException{
		rmiQueue.remove(id);
	}

	@Override
	public void sendMessage(UUID id, String message) throws RemoteException{
		GameMessage gMessage = new GameMessage(message, id);
		MessageManager.getInstance().putRecivedMessage(gMessage);
	}

	@Override
	public String getMessage(UUID id) throws RemoteException{
		try {
			return rmiQueue.get(id).take();
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		}
		return null;
	}

	@Override
	public boolean hadMessage(UUID id) throws RemoteException{
		return (!rmiQueue.get(id).isEmpty());
	}
}