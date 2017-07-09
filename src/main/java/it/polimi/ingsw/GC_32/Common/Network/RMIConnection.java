package it.polimi.ingsw.GC_32.Common.Network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * RMI connection interface. The method here presented can be invoched remotely by the client and represent all the functionality offered by the remote RMIServer
 *
 *@see RMIServer
 */

public interface RMIConnection extends Remote{
	
	/**
	 * open a new connection
	 * @return the UUID of the client just connected
	 * @throws RemoteException
	 */
	UUID open() throws RemoteException;
	
	/**
	 * close the RMI connection established with the server
	 * @param id the UUID of the client who wants to disconnect
	 * @throws RemoteException
	 */
	void close(UUID id) throws RemoteException;
	
	/**
	 * allows to send a message to the server
	 * @param id the UUID of the player who sent the message
	 * @param Message the message which must be sent to the server. According to client network architecture, all messages sent by the client are string (generally
	 * @throws RemoteException
	 */
	void sendMessage(UUID id, String Message) throws RemoteException;
	
	/**
	 * allows to retrive messages from the server
	 * @param id the UUID of the client who want retrive the message
	 * @return the message sent by the server
	 * @throws RemoteException
	 */
	String getMessage(UUID id) throws RemoteException;
	
	/**
	 * tells if there are messages for the client
	 * @return true if the server has sent a message to the client which must be get, false otherwise
	 * @throws IOException
	 */
	boolean hadMessage(UUID id) throws RemoteException;
}