package it.polimi.ingsw.GC_32.Client.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.GC_32.Common.Network.MsgConnection;
import it.polimi.ingsw.GC_32.Common.Utils.KillableRunnable;

/**
 * client-side network thread which handles the socket connection. It has some interal queues to avoid possibly loss of packets of out-of-sequence receptions.
 * 
 * <ul>
 * <li>{@link #socket}: the socket bind to the port and ip address indicated into the class constructor</li>
 * <li>{@link #in}: scanner which wraps the socket inputstream</li>
 * <li>{@link #out}: printwriter which wraps the socket outputstram</li>
 * <li>{@link #stop}: flag used to stop this thread</li>
 * <li>{@link #receivedMessageQueue}: queue used for memorized messages recived on the socket</li>
 * <li>{@link #sendMessageQueue}: queue used to send message to the server</lI>
 * </ul>
 *
 */

public class SocketMsgConnection implements MsgConnection, KillableRunnable{
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());
    
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private boolean stop = false;
	
	private ConcurrentLinkedQueue<String> sendMessageQueue;
	private ConcurrentLinkedQueue<String> receivedMessageQueue;
	
	/**
	 * open the socket on the specified port and IP address
	 * 
	 * @param ip the IP address of the server
	 * @param port the port used to communicate with the server application
	 * @throws IOException
	 */
	public void open(String ip, int port) throws IOException{
		socket = new Socket(ip, port);
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream());
		
		this.sendMessageQueue = new ConcurrentLinkedQueue<String>();
		this.receivedMessageQueue = new ConcurrentLinkedQueue<String>();
						
	}
	
	/**
	 * run method handles the sending and the reception of messages from the network, continously looking if there are bytes ready to be read from the socket inputstram
	 * (reception phase) or if the sendMessageQueue isn't empty (sending phase). Messages are in this way sent to the server or received from the server.
	 */
	public void run(){
		
		while(!stop){
			
			try {
				if(socket.getInputStream().available()>0){
					receivedMessageQueue.add(in.nextLine());
				}
			} catch (IOException e) {
			    LOGGER.log(Level.INFO, "Connection cloased unexpectedly", e);
				break;
			}
			
			if(!sendMessageQueue.isEmpty()){
				out.println(sendMessageQueue.poll());
				out.flush();
			}
		}
	}
	
	/**
	 * close the socket connection
	 */
	public void close() throws IOException{
		socket.close();
		in.close();
		out.close();
	}
	
	/**
	 * send the specifiy message to the server
	 * @param message the message to send
	 */
	public void sendMessage(String message){
		this.sendMessageQueue.add(message);
	}
	
	/**
	 * get messages from the receivedMessageQueue
	 * @return message to be read
	 */
	public String getMessage(){
		return this.receivedMessageQueue.poll();
	}
	
	/**
	 * tells if there are some message to poll from the queue
	 * @return true if the receivedMessageQueue isn't empty, false otherwise
	 */
	public boolean hasMessage() throws IOException{
		return !this.receivedMessageQueue.isEmpty();
	}
	
	/**
	 * stop this thread
	 */
	@Override
	public void kill(){
	    this.stop = true;
	}
}
