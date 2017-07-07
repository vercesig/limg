package it.polimi.ingsw.GC_32.Server.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * class containing all the information about the socket connection of a player
 * 
 * <ul>
 *  <li> {@link #socket}: the socket object associated to the connection
 *  <li> {@link #in}: the scanner open on the inputStream of the socket, used to recive messages
 *  <li> {@link #out}: the PrintWriter open on the outputStream of the socket, used to send message
 * </ul>
 * <p>
 *
 */
public class SocketInfoContainer {

	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	
	/**
	 * instanziate a new SocketInfoContainer. In particular the constructor is responsable of opening the input and the output stream of the socket
	 * 
	 * @param socket the socket object used to create this ScoketInfoContainer
	 * @throws IOException
	 */
	public SocketInfoContainer(Socket socket) throws IOException{
		this.socket = socket;
		this.in = new Scanner(this.socket.getInputStream());
		this.out = new PrintWriter(this.socket.getOutputStream());
	}
	
	/**
	 * allow to get the socket object of this SocketInfoContainer
	 * 
	 * @return the socket of this container
	 */
	public Socket getSocket(){
		return this.socket;
	}
	
	/**
	 * allow to get the Scanner object of this SocketInfoContainer
	 * 
	 * @return the Scanner of this container
	 */
	public Scanner getScannerIn(){
		return this.in;
	}
	
	/**
	 * allow to get the PrintWriter object of this SocketInfoContainer
	 * 
	 * @return the PrintWriter of this container
	 */
	public PrintWriter getPrinterOut(){
		return this.out;
	}
	
	/**
	 * close this SocketInfoContainer. So close the socket and all the resources linked to it (input and output stream)
	 * @throws IOException
	 */
	public void close() throws IOException{
		this.socket.close();
		this.in.close();
		this.out.close();
	}
	
}
