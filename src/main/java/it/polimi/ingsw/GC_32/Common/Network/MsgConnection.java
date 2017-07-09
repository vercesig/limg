package it.polimi.ingsw.GC_32.Common.Network;

import java.io.IOException;

/**
 * interface implemented by the client network classes which define all the functionality offered by the client network architecture. MsgConnection will be implemented both
 * by socket classes which handle the communication via socket and RMI classes which handle the RMI communication mode.
 * 
 */

public interface MsgConnection{
	
	/**
	 * open the network connection
	 * @param ip the server IP address
	 * @param port the port used by the server to accept new connections
	 * @throws IOException
	 */
	public void open(String ip, int port) throws IOException;
	
	/**
	 * close the network connection that was precedently opened
	 * @throws IOException
	 */
	public void close() throws IOException;
	
	/**
	 * send a message to the server, a client message is simply a string (generally it will be the string representation of a JSON packet, which will be parsed and then
	 * processed by the Game thread)
	 * @param message the mesasge to send
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException;
	
	/**
	 * allow to retrive messages sent by the server to the client
	 * @return the message which has been sent by the server
	 * @throws IOException
	 */
	public String getMessage() throws IOException;
	
	/**
	 * tells if there are messages for the client
	 * @return true if the server has sent a message to the client which must be get, false otherwise
	 * @throws IOException
	 */
	public boolean hasMessage() throws IOException;
}