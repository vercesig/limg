package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import it.polimi.ingsw.GC_32.Common.Network.RMIConnection;

import it.polimi.ingsw.GC_32.Common.Network.MsgConnection;

/**
 * client-side class which handle the interaction with the server throught RMI.
 * 
 * <ul>
 * <li>{@link #serverConn}: the instance of the remote server</li>
 * <li>{@link #playerID}: the UUID of the player needed by the interaction with the remote server</li>
 * </ul>
 *
 * @see RMIConnection
 */

public class RMIMsgConnection implements MsgConnection{
	private RMIConnection serverConn;
	private UUID playerID;
	
	/**
	 * open the RMI connection on the specified port and IP address
	 * 
	 * @param ip the IP address of the server
	 * @param port the port used to communicate with the server application
	 * @throws IOException
	 */
	@Override
	public void open(String ip, int port) throws IOException{
		Registry rmiRegistry = LocateRegistry.getRegistry(ip, port);
		try{
			this.serverConn = (RMIConnection) rmiRegistry.lookup("LIMG_Conn");
		} catch(NotBoundException e){
			throw new IOException("Server Connection Object not Found", e);
		}
		this.playerID = this.serverConn.open();
	}

	
	/**
	 * close the RMI connection
	 */
	@Override
	public void close() throws IOException{
		this.serverConn.close(playerID);
	}
	
	/**
	 * send the specifiy message to the server
	 * @param message the message to send
	 */
	@Override
	public void sendMessage(String message) throws IOException{
		this.serverConn.sendMessage(playerID, message);
	}

	/**
	 * get messages from the server queue
	 * @return message to be read
	 */
	@Override
	public String getMessage() throws IOException{
		return this.serverConn.getMessage(playerID);
	}

	/**
	 * tells if there are some message to poll from the server message queue
	 * @return true if the server has some messages, false otherwise
	 */
	@Override
	public boolean hasMessage() throws IOException {
		return this.serverConn.hadMessage(playerID);
	}
}